package com.bbangle.bbangle.board.repository;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.domain.Product;
import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.board.domain.QProduct;
import com.bbangle.bbangle.board.domain.QProductImg;
import com.bbangle.bbangle.board.domain.TagEnum;
import com.bbangle.bbangle.board.dto.BoardAvailableDayDto;
import com.bbangle.bbangle.board.dto.BoardDetailDto;
import com.bbangle.bbangle.board.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.board.dto.BoardImgDto;
import com.bbangle.bbangle.board.dto.BoardResponseDto;
import com.bbangle.bbangle.board.dto.ProductBoardLikeStatus;
import com.bbangle.bbangle.board.dto.ProductDto;
import com.bbangle.bbangle.board.dto.ProductTagDto;
import com.bbangle.bbangle.common.sort.SortType;
import com.bbangle.bbangle.store.domain.QStore;
import com.bbangle.bbangle.store.dto.StoreDto;
import com.bbangle.bbangle.wishListBoard.domain.QWishlistProduct;
import com.bbangle.bbangle.wishListFolder.domain.QWishlistFolder;
import com.bbangle.bbangle.wishListFolder.domain.WishlistFolder;
import com.bbangle.bbangle.wishListStore.domain.QWishlistStore;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import com.bbangle.bbangle.exception.CategoryTypeException;
import com.bbangle.bbangle.util.SecurityUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardResponseDto> getBoardResponseDto(
        String sort, Boolean glutenFreeTag, Boolean highProteinTag,
        Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
        String category, Integer minPrice, Integer maxPrice,
        Boolean orderAvailableToday
    ) {

        QBoard board = QBoard.board;
        QProduct product = QProduct.product;
        QStore store = QStore.store;

        BooleanBuilder filter =
            setFilteringCondition(glutenFreeTag,
                highProteinTag,
                sugarFreeTag,
                veganTag,
                ketogenicTag,
                category,
                minPrice,
                maxPrice,
                product,
                board,
                orderAvailableToday);


        List<Board> boards = queryFactory
            .selectFrom(board)
            .leftJoin(board.productList, product)
            .fetchJoin()
            .leftJoin(board.store, store)
            .fetchJoin()
            .where(filter)
            .fetch();

        Map<Long, List<ProductTagDto>> productTagsByBoardId = getLongListMap(boards);

        List<BoardResponseDto> content = new ArrayList<>();

        // isBundled 포함한 정리
        for (Board board1 : boards) {
            List<String> tags = addList(productTagsByBoardId.get(board1.getId()));
            content.add(BoardResponseDto.from(board1, tags));
        }

        return content;
    }

    @Override
    public Slice<BoardResponseDto> getAllByFolder(
        String sort, Pageable pageable, Long wishListFolderId,
        WishlistFolder selectedFolder
    ) {
        QBoard board = QBoard.board;
        QProduct product = QProduct.product;
        QStore store = QStore.store;
        QWishlistProduct products = QWishlistProduct.wishlistProduct;
        QWishlistFolder folder = QWishlistFolder.wishlistFolder;

        OrderSpecifier<?> orderSpecifier = sortTypeFolder(sort, board, products);

        List<Board> boards = queryFactory
            .selectFrom(board)
            .leftJoin(board.productList, product)
            .fetchJoin()
            .leftJoin(board.store, store)
            .fetchJoin()
            .join(board)
            .on(board.id.eq(products.board.id))
            .join(products)
            .on(products.wishlistFolder.eq(folder))
            .where(products.wishlistFolder.eq(selectedFolder)
                .and(products.isDeleted.eq(false)))
            .offset(pageable.getOffset())
            .orderBy(orderSpecifier)
            .limit(pageable.getPageSize() + 1)
            .fetch();

        Map<Long, List<ProductTagDto>> productTagsByBoardId = getLongListMap(boards);

        List<BoardResponseDto> content = new ArrayList<>();

        // isBundled 포함한 정리
        for (Board board1 : boards) {
            List<String> tags = addList(productTagsByBoardId.get(board1.getId()));
            content.add(BoardResponseDto.inFolder(board1, tags));
        }

        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private static OrderSpecifier<?> sortTypeFolder(
        String sort,
        QBoard board,
        QWishlistProduct products
    ) {
        OrderSpecifier<?> orderSpecifier;
        if (sort == null) {
            orderSpecifier = products.createdAt.desc();
            return orderSpecifier;
        }
        switch (SortType.fromString(sort)) {
            case RECENT:
                orderSpecifier = products.createdAt.desc();
                break;
            case LOW_PRICE:
                orderSpecifier = board.price.asc();
                break;
            case POPULAR:
                orderSpecifier = board.wishCnt.desc();
                break;
            default:
                throw new IllegalArgumentException("Invalid SortType");
        }
        return orderSpecifier;
    }

    private static Map<Long, List<ProductTagDto>> getLongListMap(List<Board> boards) {
        Map<Long, List<ProductTagDto>> productTagsByBoardId = new HashMap<>();
        for (Board board1 : boards) {
            for (Product product1 : board1.getProductList()) {
                productTagsByBoardId.put(board1.getId(),
                    productTagsByBoardId.getOrDefault(board1.getId(), new ArrayList<>()));
                productTagsByBoardId.get(board1.getId())
                    .add(ProductTagDto.from(product1));
            }
        }
        return productTagsByBoardId;
    }

    private static BooleanBuilder setFilteringCondition(
        Boolean glutenFreeTag, Boolean highProteinTag,
        Boolean sugarFreeTag,
        Boolean veganTag, Boolean ketogenicTag, String category,
        Integer minPrice, Integer maxPrice,
        QProduct product, QBoard board,
        Boolean orderAvailableToday
    ) {

        BooleanBuilder filterBuilder = new BooleanBuilder();
        if (glutenFreeTag != null) {
            filterBuilder.and(product.glutenFreeTag.eq(glutenFreeTag));
        }
        if (highProteinTag != null) {
            filterBuilder.and(product.highProteinTag.eq(highProteinTag));
        }
        if (sugarFreeTag != null) {
            filterBuilder.and(product.sugarFreeTag.eq(sugarFreeTag));
        }
        if (veganTag != null) {
            filterBuilder.and(product.veganTag.eq(veganTag));
        }
        if (ketogenicTag != null) {
            filterBuilder.and(product.ketogenicTag.eq(ketogenicTag));
        }
        if (category != null && !category.isBlank()) {
            if (!Category.checkCategory(category)) {
                throw new CategoryTypeException();
            }
            filterBuilder.and(product.category.eq(Category.valueOf(category)));
        }
        if (minPrice != null) {
            filterBuilder.and(board.price.goe(minPrice));
        }
        if (maxPrice != null) {
            filterBuilder.and(board.price.loe(maxPrice));
        }
        if (orderAvailableToday != null && orderAvailableToday) {
            DayOfWeek dayOfWeek = LocalDate.now()
                .getDayOfWeek();

            switch (dayOfWeek){
                case MONDAY -> filterBuilder.and(board.monday.eq(true));
                case TUESDAY -> filterBuilder.and(board.tuesday.eq(true));
                case WEDNESDAY -> filterBuilder.and(board.wednesday.eq(true));
                case THURSDAY -> filterBuilder.and(board.thursday.eq(true));
                case FRIDAY -> filterBuilder.and(board.friday.eq(true));
                case SATURDAY -> filterBuilder.and(board.saturday.eq(true));
                case SUNDAY -> filterBuilder.and(board.sunday.eq(true));
            }
        }
        return filterBuilder;
    }

    private List<String> addList(List<ProductTagDto> dtos) {
        List<String> tags = new ArrayList<>();
        boolean glutenFreeTag = false;
        boolean highProteinTag = false;
        boolean sugarFreeTag = false;
        boolean veganTag = false;
        boolean ketogenicTag = false;
        if (dtos == null) {
            return tags;
        }
        for (ProductTagDto dto : dtos) {
            if (dto.glutenFreeTag()) {
                glutenFreeTag = true;
            }
            if (dto.highProteinTag()) {
                highProteinTag = true;
            }
            if (dto.sugarFreeTag()) {
                sugarFreeTag = true;
            }
            if (dto.veganTag()) {
                veganTag = true;
            }
            if (dto.ketogenicTag()) {
                ketogenicTag = true;
            }
        }
        if (glutenFreeTag) {
            tags.add(TagEnum.GLUTEN_FREE.label());
        }
        if (highProteinTag) {
            tags.add(TagEnum.HIGH_PROTEIN.label());
        }
        if (sugarFreeTag) {
            tags.add(TagEnum.SUGAR_FREE.label());
        }
        if (veganTag) {
            tags.add(TagEnum.VEGAN.label());
        }
        if (ketogenicTag) {
            tags.add(TagEnum.KETOGENIC.label());
        }
        return tags;
    }

    @Override
    public BoardDetailResponseDto getBoardDetailResponseDtoWithLike(Long memberId, Long boardId) {
        QBoard board = QBoard.board;
        QProduct product = QProduct.product;
        QStore store = QStore.store;
        QProductImg productImg = QProductImg.productImg;

        QWishlistProduct wishlistProduct = QWishlistProduct.wishlistProduct;
        QWishlistStore wishlistStore = QWishlistStore.wishlistStore;

        List<Tuple> fetch = queryFactory
            .select(
                store.id,
                store.name,
                store.profile,
                wishlistStore.id,
                board.id,
                board.profile,
                board.title,
                board.price,
                productImg.id,
                productImg.url,
                board.title,
                board.price,
                board.monday,
                board.tuesday,
                board.wednesday,
                board.thursday,
                board.friday,
                board.saturday,
                board.sunday,
                board.purchaseUrl,
                board.detail,
                wishlistProduct.id,
                product.id,
                product.title,
                product.category,
                product.glutenFreeTag,
                product.highProteinTag,
                product.sugarFreeTag,
                product.veganTag,
                product.ketogenicTag
            )
            .from(product)
            .where(board.id.eq(boardId))
            .join(product.board, board)
            .join(board.store, store)
            .leftJoin(productImg)
            .on(board.id.eq(productImg.board.id))
            .leftJoin(wishlistProduct)
            .on(wishlistProduct.board.eq(board), wishlistProduct.memberId.eq(memberId),
                wishlistProduct.isDeleted.eq(false))
            .leftJoin(wishlistStore)
            .on(wishlistStore.store.eq(store), wishlistStore.member.id.eq(memberId),
                wishlistStore.isDeleted.eq(false))
            .fetch();

        int index = 0;
        int resultSize = fetch.size();
        StoreDto storeDto = null;
        BoardDetailDto boardDto = null;
        List<ProductDto> productDtos = new ArrayList<>();
        Set<BoardImgDto> boardImgDtos = new HashSet<>();
        Set<String> allTags = new HashSet<>();
        Set<Category> categories = new HashSet<>();
        List<String> tags = new ArrayList<>();

        for (Tuple tuple : fetch) {
            index++;

            if (tuple.get(product.glutenFreeTag)) {
                tags.add(TagEnum.GLUTEN_FREE.label());
            }
            if (tuple.get(product.highProteinTag)) {
                tags.add(TagEnum.HIGH_PROTEIN.label());
            }
            if (tuple.get(product.sugarFreeTag)) {
                tags.add(TagEnum.SUGAR_FREE.label());
            }
            if (tuple.get(product.veganTag)) {
                tags.add(TagEnum.VEGAN.label());
            }
            if (tuple.get(product.ketogenicTag)) {
                tags.add(TagEnum.KETOGENIC.label());
            }
            categories.add(tuple.get(product.category));
            allTags.addAll(tags);

            boardImgDtos.add(
                BoardImgDto.builder()
                    .id(tuple.get(productImg.id))
                    .url(tuple.get(productImg.url))
                    .build()
            );

            productDtos.add(
                ProductDto.builder()
                    .id(tuple.get(product.id))
                    .title(tuple.get(product.title))
                    .category(tuple.get(product.category))
                    .tags(new ArrayList<>(tags))
                    .build()
            );

            tags.clear();

            if (index == resultSize) {
                storeDto = StoreDto.builder()
                    .storeId(tuple.get(store.id))
                    .storeName(tuple.get(store.name))
                    .profile(tuple.get(store.profile))
                    .isWished(tuple.get(wishlistStore.id) != null ? true : false)
                    .build();

                boardDto = BoardDetailDto.builder()
                    .boardId(tuple.get(board.id))
                    .thumbnail(tuple.get(board.profile))
                    .title(tuple.get(board.title))
                    .price(tuple.get(board.price))
                    .orderAvailableDays(
                        BoardAvailableDayDto.builder()
                            .mon(tuple.get(board.monday))
                            .tue(tuple.get(board.tuesday))
                            .wed(tuple.get(board.wednesday))
                            .thu(tuple.get(board.thursday))
                            .fri(tuple.get(board.friday))
                            .sat(tuple.get(board.saturday))
                            .sun(tuple.get(board.sunday))
                            .build()
                    )
                    .purchaseUrl(tuple.get(board.purchaseUrl))
                    .detail(tuple.get(board.detail))
                    .products(productDtos)
                    .images(boardImgDtos.stream()
                        .toList())
                    .tags(allTags.stream()
                        .toList())
                    .isWished(tuple.get(wishlistProduct.id) != null ? true : false)
                    .isBundled(categories.size() > 1)
                    .build();
            }
        }

        return BoardDetailResponseDto.builder()
            .store(storeDto)
            .board(boardDto)
            .build();
    }

    @Override
    public BoardDetailResponseDto getBoardDetailResponseDto(Long boardId) {
        QBoard board = QBoard.board;
        QProduct product = QProduct.product;
        QStore store = QStore.store;
        QProductImg productImg = QProductImg.productImg;

        List<Tuple> fetch = queryFactory
            .select(
                store.id,
                store.name,
                store.profile,
                board.id,
                board.profile,
                board.title,
                board.price,
                productImg.id,
                productImg.url,
                board.title,
                board.price,
                board.monday,
                board.tuesday,
                board.wednesday,
                board.thursday,
                board.friday,
                board.saturday,
                board.sunday,
                board.purchaseUrl,
                board.detail,
                product.id,
                product.title,
                product.category,
                product.glutenFreeTag,
                product.highProteinTag,
                product.sugarFreeTag,
                product.veganTag,
                product.ketogenicTag
            )
            .from(product)
            .where(board.id.eq(boardId))
            .join(product.board, board)
            .join(board.store, store)
            .leftJoin(productImg)
            .on(board.id.eq(productImg.board.id))
            .fetch();

        int index = 0;
        int resultSize = fetch.size();
        StoreDto storeDto = null;
        BoardDetailDto boardDto = null;
        Set<ProductDto> productDtos = new HashSet<>();
        Set<BoardImgDto> boardImgDtos = new HashSet<>();
        Set<String> allTags = new HashSet<>();
        Set<Category> categories = new HashSet<>();
        List<String> tags = new ArrayList<>();

        for (Tuple tuple : fetch) {
            index++;

            if (tuple.get(product.glutenFreeTag)) {
                tags.add(TagEnum.GLUTEN_FREE.label());
            }
            if (tuple.get(product.highProteinTag)) {
                tags.add(TagEnum.HIGH_PROTEIN.label());
            }
            if (tuple.get(product.sugarFreeTag)) {
                tags.add(TagEnum.SUGAR_FREE.label());
            }
            if (tuple.get(product.veganTag)) {
                tags.add(TagEnum.VEGAN.label());
            }
            if (tuple.get(product.ketogenicTag)) {
                tags.add(TagEnum.KETOGENIC.label());
            }
            categories.add(tuple.get(product.category));
            allTags.addAll(tags);

            boardImgDtos.add(
                BoardImgDto.builder()
                    .id(tuple.get(productImg.id))
                    .url(tuple.get(productImg.url))
                    .build()
            );

            productDtos.add(
                ProductDto.builder()
                    .id(tuple.get(product.id))
                    .title(tuple.get(product.title))
                    .category(tuple.get(product.category))
                    .tags(new ArrayList<>(tags))
                    .build()
            );

            tags.clear();

            if (index == resultSize) {
                storeDto = StoreDto.builder()
                    .storeId(tuple.get(store.id))
                    .storeName(tuple.get(store.name))
                    .profile(tuple.get(store.profile))
                    .isWished(false)
                    .build();

                boardDto = BoardDetailDto.builder()
                    .boardId(tuple.get(board.id))
                    .thumbnail(tuple.get(board.profile))
                    .title(tuple.get(board.title))
                    .price(tuple.get(board.price))
                    .orderAvailableDays(
                        BoardAvailableDayDto.builder()
                            .mon(tuple.get(board.monday))
                            .tue(tuple.get(board.tuesday))
                            .wed(tuple.get(board.wednesday))
                            .thu(tuple.get(board.thursday))
                            .fri(tuple.get(board.friday))
                            .sat(tuple.get(board.saturday))
                            .sun(tuple.get(board.sunday))
                            .build()
                    )
                    .purchaseUrl(tuple.get(board.purchaseUrl))
                    .detail(tuple.get(board.detail))
                    .products(productDtos.stream()
                        .toList())
                    .images(boardImgDtos.stream()
                        .toList())
                    .tags(allTags.stream()
                        .toList())
                    .isWished(false)
                    .isBundled(categories.size() > 1)
                    .build();
            }
        }

        return BoardDetailResponseDto.builder()
            .store(storeDto)
            .board(boardDto)
            .build();
    }


    @Override
    public HashMap<Long, String> getAllBoardTitle() {
        QBoard board = QBoard.board;

        List<Tuple> fetch = queryFactory
            .select(board.id, board.title)
            .from(board)
            .fetch();

        HashMap<Long, String> boardMap = new HashMap<>();
        fetch.forEach((tuple) -> boardMap.put(tuple.get(board.id), tuple.get(board.title)));

        return boardMap;
    }

    @Override
    public List<BoardResponseDto> updateLikeStatus(
        List<Long> matchedIdx,
        List<BoardResponseDto> content
    ) {
        QBoard board = QBoard.board;
        QWishlistProduct wishlistProduct = QWishlistProduct.wishlistProduct;

        Long memberId = SecurityUtils.getMemberId();

        BooleanExpression isLikedExpression = wishlistProduct.isDeleted.isFalse();

        List<ProductBoardLikeStatus> likeFetch = queryFactory
            .select(Projections.bean(
                ProductBoardLikeStatus.class,
                board.id.as("boardId"),
                isLikedExpression.as("isLike")
            ))
            .from(board)
            .leftJoin(wishlistProduct)
            .on(board.id.eq(wishlistProduct.board.id)
                .and(wishlistProduct.memberId.eq(memberId)))
            .where(board.id.in(matchedIdx))
            .fetch()
            .stream()
            .peek(result -> {
                if (result.getIsLike() == null) {
                    result.setIsLike(false);
                }
            })
            .toList();

        for (ProductBoardLikeStatus likeStatus : likeFetch) {
            if (likeStatus.getIsLike()) {
                for (BoardResponseDto boardResponseDto : content) {
                    if (Objects.equals(likeStatus
                        .getBoardId(), boardResponseDto
                        .boardId())) {
                        boardResponseDto
                            .updateLike(true);
                    }
                }
            }
        }
        return content;
    }

}
