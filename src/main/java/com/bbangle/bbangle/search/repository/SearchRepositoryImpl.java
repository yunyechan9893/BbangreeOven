package com.bbangle.bbangle.search.repository;

import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.board.domain.QProduct;
import com.bbangle.bbangle.board.domain.TagEnum;
import com.bbangle.bbangle.board.dto.BoardResponseDto;
import com.bbangle.bbangle.common.sort.SortType;
import com.bbangle.bbangle.exception.CategoryTypeException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.search.domain.QSearch;
import com.bbangle.bbangle.search.dto.KeywordDto;
import com.bbangle.bbangle.search.dto.SearchBoardDto;
import com.bbangle.bbangle.store.domain.QStore;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import com.bbangle.bbangle.wishListBoard.domain.QWishlistProduct;
import com.bbangle.bbangle.wishListStore.domain.QWishlistStore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchQueryDSLRepository {
    private final JPAQueryFactory queryFactory;
    private final int ONEDAY = 24;
    private final int DEFAULT_ITEM_SIZE = 10;



    @Override
    public SearchBoardDto getSearchResult(Long memberId, List<Long> boardIds, String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                          Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag, Boolean orderAvailableToday,
                                          String category, Integer minPrice, Integer maxPrice, Pageable pageable) {
        QBoard board = QBoard.board;
        QProduct product = QProduct.product;
        QStore store = QStore.store;

        QWishlistProduct wishlistProduct = QWishlistProduct.wishlistProduct;

        BooleanBuilder filter =
                setFilteringCondition(glutenFreeTag,
                        highProteinTag,
                        sugarFreeTag,
                        veganTag,
                        ketogenicTag,
                        orderAvailableToday,
                        category,
                        minPrice,
                        maxPrice,
                        product,
                        board);

        var orderByBuilder =
                sort.equals(SortType.POPULAR.getValue())?
                        board.view.add(board.wishCnt.multiply(10)).desc():
                        orderByFieldList(boardIds, product.board.id);


        var defaultQuery = queryFactory
                .select(product.board.id)
                .distinct()
                .from(product)
                .where(

                        board.id.in(boardIds),
                        filter
                )
                .orderBy(orderByBuilder);

        var queryAllCount = defaultQuery.fetch().stream().toList().size();

        if (queryAllCount <= 0){
            return SearchBoardDto.builder()
                    .content(List.of())
                    .itemAllCount(0)
                    .pageNumber(pageable.getPageNumber())
                    .limitItemCount(DEFAULT_ITEM_SIZE)
                    .currentItemCount(0)
                    .existNextPage(false)
                    .build();
        }

        var filterQuery = defaultQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Expression<?>> columns = new ArrayList<>();
        columns.add(product.board.store.id);
        columns.add(product.board.store.name);
        columns.add(product.board.id);
        columns.add(product.board.profile);
        columns.add(product.board.title);
        columns.add(product.board.price);
        columns.add(product.category);
        columns.add(product.glutenFreeTag);
        columns.add(product.highProteinTag);
        columns.add(product.sugarFreeTag);
        columns.add(product.veganTag);
        columns.add(product.ketogenicTag);

        if (memberId != null && memberId > 0) {
            columns.add(wishlistProduct.id);
        }

        var boards = queryFactory
                .select(columns.toArray(new Expression[0]))
                .from(product)
                .join(product.board, board)
                .join(board.store, store)
                .where(board.id.in(filterQuery))
                .orderBy(orderByFieldList(filterQuery, product.board.id));


        if (memberId != null && memberId > 0) {
            boards = boards.leftJoin(wishlistProduct).on(wishlistProduct.board.eq(board), wishlistProduct.memberId.eq(memberId), wishlistProduct.isDeleted.eq(false));
        }


        Map<Long, BoardResponseDto> boardMap = new LinkedHashMap<>();
        Set<Category> categories = new HashSet<>();

        for (Tuple tuple:boards.fetch()) {
            Long boardId =  tuple.get(product.board.id);

            if (!boardMap.containsKey(boardId)) {
                boardMap.put(boardId,
                        BoardResponseDto.builder()
                                .boardId(boardId)
                                .storeId(tuple.get(product.board.store.id))
                                .storeName(tuple.get(product.board.store.name))
                                .thumbnail(tuple.get(product.board.profile))
                                .title(tuple.get(product.board.title))
                                .price(tuple.get(product.board.price))
                                .isBundled(categories.size() > 1)
                                .tags(new ArrayList<>())
                                .isWished(false)
                                .build());

                categories.clear();
            }

            categories.add(tuple.get(product.category));

            BoardResponseDto boardResponseDto = boardMap.get(tuple.get(product.board.id));

            if (tuple.get(product.glutenFreeTag)) {
                boardMap.get(boardId).tags().add(TagEnum.GLUTEN_FREE.label());
            }
            if (tuple.get(product.highProteinTag)) {
                boardMap.get(boardId).tags().add(TagEnum.HIGH_PROTEIN.label());
            }
            if (tuple.get(product.sugarFreeTag)) {
                boardMap.get(boardId).tags().add(TagEnum.SUGAR_FREE.label());
            }
            if (tuple.get(product.veganTag)) {
                boardMap.get(boardId).tags().add(TagEnum.VEGAN.label());
            }
            if (tuple.get(product.ketogenicTag)) {
                boardMap.get(boardId).tags().add(TagEnum.KETOGENIC.label());
            }

            boardMap.put(tuple.get(product.board.id), boardResponseDto);
        }

        var content = boardMap.entrySet().stream().map(
                longBoardResponseDtoEntry -> longBoardResponseDtoEntry.getValue()
        ).map(
                boardResponseDto -> removeDuplicatesFromDto(boardResponseDto)
        ).toList();

        return SearchBoardDto.builder()
                .content(content)
                .pageNumber(pageable.getPageNumber())
                .itemAllCount(queryAllCount)
                .limitItemCount(DEFAULT_ITEM_SIZE)
                .currentItemCount(content.size())
                .existNextPage(queryAllCount - ((pageable.getPageNumber() + 1) * DEFAULT_ITEM_SIZE) > 0)
                .build();
    }
    private OrderSpecifier<?> orderByFieldList(List<Long> boardIds, NumberPath<Long> id) {
        String ids = boardIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
        return Expressions.stringTemplate("FIELD({0}, " + ids + ")", id).asc();
    }

    @Override
    public List<StoreResponseDto> getSearchedStore(Long memberId, List<Long> storeIndexList, Pageable pageable){
        QStore store = QStore.store;
        QWishlistStore wishlistStore = QWishlistStore.wishlistStore;

        List<Expression<?>> columns = new ArrayList<>();
        columns.add(store.id);
        columns.add(store.name);
        columns.add(store.introduce);
        columns.add(store.profile);

        if (memberId > 1L){
            columns.add(wishlistStore.id);
        }

        var stores = queryFactory
                .select(columns.toArray(new Expression[0]))
                .from(store)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(store.id.in(storeIndexList))
                .orderBy(orderByFieldList(storeIndexList, store.id));

        if (memberId > 1L){
            stores.leftJoin(wishlistStore).on(wishlistStore.store.eq(store), wishlistStore.member.id.eq(memberId), wishlistStore.isDeleted.eq(false));
        }



        return stores.fetch().stream().map(
                tuple -> StoreResponseDto.builder()
                        .storeId(tuple.get(store.id))
                        .storeName(tuple.get(store.name))
                        .introduce(tuple.get(store.introduce))
                        .profile(tuple.get(store.profile))
                        .isWished(tuple.get(wishlistStore.id)!=null?true:false)
                        .build()).toList();
    }



    private static BoardResponseDto removeDuplicatesFromDto(BoardResponseDto boardResponseDto) {
        List<String> uniqueTags = boardResponseDto.tags().stream().distinct().collect(Collectors.toList());

        return BoardResponseDto.builder()
                .boardId(boardResponseDto.boardId())
                .storeId(boardResponseDto.storeId())
                .storeName(boardResponseDto.storeName())
                .thumbnail(boardResponseDto.thumbnail())
                .title(boardResponseDto.title())
                .price(boardResponseDto.price())
                .isBundled(boardResponseDto.isBundled())
                .isWished(boardResponseDto.isWished())
                .tags(uniqueTags)
                .build();
    }

    @Override
    public List<KeywordDto> getRecencyKeyword(Member member) {
        QSearch search = QSearch.search;

        return queryFactory.select(search.keyword, search.createdAt.max())
                .from(search)
                .where(search.isDeleted.eq(false), search.member.eq(member))
                .groupBy(search.keyword)
                .orderBy(search.createdAt.max().desc())
                .limit(7)
                .fetch().stream().map(tuple -> new KeywordDto(tuple.get(search.keyword)))
                .toList();
    }

    @Override
    public String[] getBestKeyword() {
        QSearch search = QSearch.search;

        // 현재시간과 하루전 시간을 가져옴
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime beforeOneDayTime = currentTime.minusHours(ONEDAY);

        // 현재시간으로부터 24시간 전 검색어를 검색수 내림 차순으로 7개 가져옴
        return queryFactory.select(search.keyword)
                .from(search)
                .where(search.createdAt.gt(beforeOneDayTime))
                .groupBy(search.keyword)
                .orderBy(search.count().desc())
                .limit(7)
                .fetch()
                .toArray(new String[0]);
    }

    @Override
    public void markAsDeleted(String keyword, Member member) {
        QSearch search = QSearch.search;
        queryFactory.update(search)
                .set(search.isDeleted, true)
                .where(
                        search.member.eq(member)
                                .and(search.keyword.eq(keyword))
                )
                .execute();
    }

    private static BooleanBuilder setFilteringCondition(Boolean glutenFreeTag, Boolean highProteinTag,
                                                        Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                                        Boolean orderAvailableToday, String category,
                                                        Integer minPrice, Integer maxPrice,
                                                        QProduct product, QBoard board) {
        BooleanBuilder filterBuilder = new BooleanBuilder();
        if (glutenFreeTag != null && glutenFreeTag == true) {
            filterBuilder.and(product.glutenFreeTag.eq(glutenFreeTag));
        }
        if (highProteinTag != null && highProteinTag == true) {
            filterBuilder.and(product.highProteinTag.eq(highProteinTag));
        }
        if (sugarFreeTag != null && sugarFreeTag == true) {
            filterBuilder.and(product.sugarFreeTag.eq(sugarFreeTag));
        }
        if (veganTag != null && veganTag == true) {
            filterBuilder.and(product.veganTag.eq(veganTag));
        }
        if (ketogenicTag != null && ketogenicTag == true) {
            filterBuilder.and(product.ketogenicTag.eq(ketogenicTag));
        }
        if (orderAvailableToday != null && orderAvailableToday == true) {
            LocalDate currentDate = LocalDate.now();
            String dayOfWeek = currentDate.getDayOfWeek().toString().substring(0, 3);
            switch (dayOfWeek){
                case "MON":
                    filterBuilder.and(board.monday.eq(true));
                    break;
                case "TUE":
                    filterBuilder.and(board.tuesday.eq(true));
                    break;
                case "WED":
                    filterBuilder.and(board.wednesday.eq(true));
                    break;
                case "THU":
                    filterBuilder.and(board.thursday.eq(true));
                    break;
                case "FRI":
                    filterBuilder.and(board.friday.eq(true));
                    break;
                case "SAT":
                    filterBuilder.and(board.saturday.eq(true));
                    break;
                case "SUN":
                    filterBuilder.and(board.sunday.eq(true));
                    break;
            }

        }
        if (category != null && !category.isBlank()) {
            if (!Category.checkCategory(category)) {
                throw new CategoryTypeException();
            }
            filterBuilder.and(product.category.eq(Category.valueOf(category)));
        }

        if (minPrice != null && minPrice!=0) {
            filterBuilder.and(board.price.goe(minPrice));
        }
        if (maxPrice != null && maxPrice!=0) {
            filterBuilder.and(board.price.loe(maxPrice));
        }
        return filterBuilder;
    }
}
