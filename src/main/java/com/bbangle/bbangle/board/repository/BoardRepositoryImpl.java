package com.bbangle.bbangle.board.repository;

import com.bbangle.bbangle.board.domain.*;
import com.bbangle.bbangle.board.dto.*;
import com.bbangle.bbangle.common.sort.SortType;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.page.BoardCustomPage;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.ranking.domain.QRanking;
import com.bbangle.bbangle.ranking.domain.Ranking;
import com.bbangle.bbangle.store.domain.QStore;
import com.bbangle.bbangle.store.dto.StoreDto;
import com.bbangle.bbangle.wishList.domain.QWishlistFolder;
import com.bbangle.bbangle.wishList.domain.QWishlistProduct;
import com.bbangle.bbangle.wishList.domain.QWishlistStore;
import com.bbangle.bbangle.wishList.domain.WishlistFolder;
import com.querydsl.jpa.impl.JPAQuery;
import java.time.DayOfWeek;
import java.time.LocalDate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import static com.bbangle.bbangle.wishList.domain.QWishlistProduct.wishlistProduct;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardQueryDSLRepository {

    private static final int PAGE_SIZE = 10;
    private static final Long EMPTY_RESULT_NEXT_CURSOR = -1L;
    private static final Double EMPTY_RESULT_SCORE = -1.0;
    private static final Boolean EMPTY_RESULT_HAS_NEXT = false;

    private final MemberRepository memberRepository;
    private final JPAQueryFactory queryFactory;
    private final QBoard board = QBoard.board;
    private final QProduct product = QProduct.product;
    private final QStore store = QStore.store;
    private final QWishlistProduct products = QWishlistProduct.wishlistProduct;
    private final QWishlistFolder folder = QWishlistFolder.wishlistFolder;
    private final QProductImg productImg = QProductImg.productImg;
    private final QBoardDetail boardDetail = QBoardDetail.boardDetail;
    private final QWishlistStore wishlistStore = QWishlistStore.wishlistStore;
    private final QRanking ranking = QRanking.ranking;

    @Override
    public BoardCustomPage<List<BoardResponseDto>> getBoardResponseList(
        FilterRequest filterRequest,
        SortType sort,
        CursorInfo cursorInfo
    ) {
        BooleanBuilder filter =
            setFilteringCondition(
                filterRequest,
                product,
                board);
        OrderSpecifier<Double> orderExpression = getOrderExpression(sort);

        BooleanBuilder cursorBuilder = setCursorBuilder(cursorInfo, sort);
        List<Long> fetch = queryFactory
            .select(board.id)
            .distinct()
            .from(product)
            .join(product.board, board)
            .join(ranking)
            .on(board.id.eq(ranking.board.id))
            .where(cursorBuilder.and(filter))
            .orderBy(orderExpression, board.id.desc())
            .limit(PAGE_SIZE + 1)
            .fetch();

        List<Board> boards = queryFactory.select(board)
            .from(board)
            .join(board.productList, product)
            .fetchJoin()
            .join(board.store, store)
            .fetchJoin()
            .join(ranking)
            .on(board.id.eq(ranking.board.id))
            .where(board.id.in(fetch))
            .orderBy(orderExpression, board.id.desc())
            .fetch();

        if (boards.isEmpty()) {
            return BoardCustomPage.from(Collections.emptyList(), EMPTY_RESULT_NEXT_CURSOR, EMPTY_RESULT_SCORE, EMPTY_RESULT_HAS_NEXT);
        }

        Map<Long, List<ProductTagDto>> productTagsByBoardId = getLongListMap(boards);
        boolean hasNext = isHasNext(boards);
        List<BoardResponseDto> content = getBoardResponseDtos(
            boards, productTagsByBoardId);

        return getBoardCustomPage(sort, cursorInfo, filter, content, hasNext);
    }

    private OrderSpecifier<Double> getOrderExpression(SortType sort) {
        if (Objects.isNull(sort) || sort.equals(SortType.RECOMMEND)) {
            return ranking.recommendScore.desc();
        }

        return ranking.popularScore.desc();
    }

    @Override
    public Slice<BoardResponseDto> getAllByFolder(
        String sort, Pageable pageable, Long wishListFolderId,
        WishlistFolder selectedFolder
    ) {
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


    private List<BoardDetailDto> fetchBoardDetails(Long boardId){
        return queryFactory.select(new QBoardDetailDto(
                        boardDetail.id,
                        boardDetail.imgIndex,
                        boardDetail.url
                ))
                .from(boardDetail)
                .where(board.id.eq(boardId))
                .fetch();
    }
    private List<String> getTagsToStringList(Product product){
        List<String> tags = new ArrayList<>();
        if (product.isGlutenFreeTag() && tags.add(TagEnum.GLUTEN_FREE.label()));
        if (product.isSugarFreeTag() && tags.add(TagEnum.SUGAR_FREE.label()));
        if (product.isHighProteinTag() && tags.add(TagEnum.HIGH_PROTEIN.label()));
        if (product.isVeganTag() && tags.add(TagEnum.VEGAN.label()));
        if (product.isKetogenicTag() && tags.add(TagEnum.KETOGENIC.label()));
        return tags;
    }

    public List<ProductDto> fetchProductDtoByBoardId(Long boardId){
        List<Product> products = queryFactory.selectFrom(product)
                .where(board.id.eq(boardId))
                .fetch();

        return products.stream().map(product1 ->
            ProductDto.builder()
                .id(product1.getId())
                .title(product1.getTitle())
                .tags(getTagsToStringList(product1))
                .category(product1.getCategory())
                .build())
                .toList();
    }

    public List<String> getProductDtosToDuplicatedTags(List<ProductDto> productDtos){
        return productDtos.stream()
                .map(productDto -> productDto.tags())
                .filter(list -> list != null)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean isBundleBoard(List<ProductDto> productDtos){
        return productDtos.stream()
                .map(productDto -> productDto.category())
                .distinct()
                .count() > 1;
    }

    private Boolean setWishlistBoard(List<Expression<?>> columns){
        columns.add(wishlistProduct.id);
        return true;
    }

    private Boolean setWishlistStore(List<Expression<?>> columns){
        columns.add(wishlistStore.id);
        return true;
    }

    private Expression[] setColomns(Long memberId){
        List<Expression<?>> columns = new ArrayList<>();
        columns.add(store.id);
        columns.add(store.name);
        columns.add(store.profile);
        columns.add(board.id);
        columns.add(board.profile);
        columns.add(board.title);
        columns.add(board.price);
        columns.add(board.monday);
        columns.add(board.tuesday);
        columns.add(board.wednesday);
        columns.add(board.thursday);
        columns.add(board.friday);
        columns.add(board.saturday);
        columns.add(board.sunday);
        columns.add(board.purchaseUrl);
        columns.add(productImg.id);
        columns.add(productImg.url);

        if (memberId != null && memberId > 0 && setWishlistBoard(columns));
        if (memberId != null && memberId > 0 && setWishlistStore(columns));

        return columns.toArray(new Expression[0]);
    }

    private JPAQuery<Tuple> getBoardDetailSelect(Long memberId){
        return queryFactory.select(setColomns(memberId));
    }

    private Boolean setWishlistJoin(JPAQuery<Tuple> jpaQuery, Long memberId){
        return jpaQuery.leftJoin(wishlistProduct)
                .on(wishlistProduct.board.id.eq(board.id),
                        wishlistProduct.memberId.eq(memberId),
                        wishlistProduct.isDeleted.eq(false))
                .leftJoin(wishlistStore)
                .on(wishlistStore.store.id.eq(store.id),
                        wishlistStore.member.id.eq(memberId),
                        wishlistStore.isDeleted.eq(false)) != null; // if문 안에 함수를 넣기 위해 사용
    }

    public List<Tuple> fetchStoreAndBoardAndImageTuple(Long memberId, Long boardId){
        JPAQuery<Tuple> jpaQuery = getBoardDetailSelect(memberId).from(board)
                .where(board.id.eq(boardId))
                .join(board.store, store)
                .leftJoin(productImg).on(board.id.eq(productImg.board.id));

        if (memberId != null && memberId > 0 && setWishlistJoin(jpaQuery, memberId));

        return jpaQuery.fetch();
    }

    private List<BoardImgDto> getBoardImageToDto(List<Tuple> tuplesRelateBoard){
        List<BoardImgDto> boardImgDtos = new ArrayList<>();
        for (Tuple tupleReleteBoard : tuplesRelateBoard) {
            boardImgDtos.add(
                    BoardImgDto.builder()
                            .id(tupleReleteBoard.get(productImg.id))
                            .url(tupleReleteBoard.get(productImg.url))
                            .build()
            );
        }

        return boardImgDtos;
    }

    @Override
    public BoardDetailResponse getBoardDetailResponse(Long memberId, Long boardId) {
        List<Tuple> tuplesRelateBoard = fetchStoreAndBoardAndImageTuple(memberId, boardId);
        List<ProductDto> productDtos = fetchProductDtoByBoardId(boardId);
        List<String> duplicatedTags = getProductDtosToDuplicatedTags(productDtos);
        Boolean isBundled = isBundleBoard(productDtos);
        List<BoardDetailDto> boardDetails = fetchBoardDetails(boardId);

        Tuple tupleReleteBoard = tuplesRelateBoard.get(0);
        List<BoardImgDto> boardImgDtos = getBoardImageToDto(tuplesRelateBoard);

        StoreDto storeDto = StoreDto.builder()
                .storeId(tupleReleteBoard.get(store.id))
                .storeName(tupleReleteBoard.get(store.name))
                .profile(tupleReleteBoard.get(store.profile))
                .isWished(tupleReleteBoard.get(wishlistStore.id) != null ? true : false)
                .build();

        BoardDetailSelectDto boardDto = BoardDetailSelectDto.builder()
                .boardId(tupleReleteBoard.get(board.id))
                .thumbnail(tupleReleteBoard.get(board.profile))
                .title(tupleReleteBoard.get(board.title))
                .price(tupleReleteBoard.get(board.price))
                .orderAvailableDays(
                        BoardAvailableDayDto.builder()
                                .mon(tupleReleteBoard.get(board.monday))
                                .tue(tupleReleteBoard.get(board.tuesday))
                                .wed(tupleReleteBoard.get(board.wednesday))
                                .thu(tupleReleteBoard.get(board.thursday))
                                .fri(tupleReleteBoard.get(board.friday))
                                .sat(tupleReleteBoard.get(board.saturday))
                                .sun(tupleReleteBoard.get(board.sunday))
                                .build()
                )
                .purchaseUrl(tupleReleteBoard.get(board.purchaseUrl))
                .detail(boardDetails)
                .products(productDtos)
                .images(boardImgDtos.stream()
                        .toList())
                .tags(duplicatedTags)
                .isWished(tupleReleteBoard.get(wishlistProduct.id) != null ? true : false)
                .isBundled(isBundled)
                .build();

        return BoardDetailResponse.builder()
            .store(storeDto)
            .board(boardDto)
            .build();
    }

    @Override
    public HashMap<Long, String> getAllBoardTitle() {
        List<Tuple> fetch = queryFactory
            .select(board.id, board.title)
            .from(board)
            .fetch();

        HashMap<Long, String> boardMap = new HashMap<>();
        fetch.forEach((tuple) -> boardMap.put(tuple.get(board.id), tuple.get(board.title)));

        return boardMap;
    }

    @Override
    public List<Board> checkingNullRanking() {
        return queryFactory.select(board)
            .from(board)
            .leftJoin(ranking)
            .on(board.eq(ranking.board))
            .where(ranking.id.isNull())
            .fetch();
    }

    @Override
    public BoardCustomPage<List<BoardResponseDto>> getBoardResponse(
        FilterRequest filterRequest,
        SortType sort,
        CursorInfo cursorId,
        Long memberId
    ) {
        BoardCustomPage<List<BoardResponseDto>> boardResponseDto = getBoardResponseList(
            filterRequest, sort, cursorId);

        if(Objects.nonNull(memberId) && memberRepository.existsById(memberId)) {
            updateLikeStatus(boardResponseDto, memberId);
        }

        return boardResponseDto;
    }

    private List<Long> extractResponseIds(BoardCustomPage<List<BoardResponseDto>> boardResponseDto) {
        return boardResponseDto.getContent()
            .stream()
            .map(BoardResponseDto::getBoardId)
            .toList();
    }

    private List<Long> getLikedContentsIds(List<Long> responseList, Long memberId) {
        return queryFactory.select(board.id)
            .from(board)
            .leftJoin(wishlistProduct)
            .on(board.eq(wishlistProduct.board))
            .where(board.id.in(responseList)
                .and(wishlistProduct.memberId.eq(memberId))
                .and(wishlistProduct.isDeleted.eq(false)))
            .fetch();
    }

    private void updateLikeStatus(
        BoardCustomPage<List<BoardResponseDto>> boardResponseDto,
        Long memberId
    ) {
        List<Long> responseList = extractResponseIds(boardResponseDto);
        List<Long> likedContentIds = getLikedContentsIds(responseList, memberId);

        for (BoardResponseDto response : boardResponseDto.getContent()) {
            for (Long likedContentId : likedContentIds) {
                if (response.getBoardId()
                    .equals(likedContentId)) {
                    response.updateLike(true);
                }
            }
        }
    }

    private BooleanBuilder setCursorBuilder(CursorInfo cursorInfo, SortType sort) {
        BooleanBuilder cursorBuilder = new BooleanBuilder();
        if (Objects.isNull(cursorInfo) || Objects.isNull(cursorInfo.targetId())) {
            return cursorBuilder;
        }
        if (Objects.isNull(sort) || sort.equals(SortType.POPULAR)) {
            cursorBuilder.and(ranking.popularScore.lt(cursorInfo.targetScore()))
                .or(ranking.popularScore.eq(cursorInfo.targetScore())
                    .and(board.id.lt(cursorInfo.targetId())));

            return cursorBuilder;
        }
        cursorBuilder.and(ranking.recommendScore.lt(cursorInfo.targetScore()))
            .or(ranking.recommendScore.eq(cursorInfo.targetScore())
                .and(board.id.lt(cursorInfo.targetId())));

        return cursorBuilder;
    }

    private BoardCustomPage<List<BoardResponseDto>> getBoardCustomPage(
        SortType sort,
        CursorInfo cursorInfo,
        BooleanBuilder filter,
        List<BoardResponseDto> content,
        boolean hasNext
    ) {
        Ranking cursorRanking = queryFactory.select(ranking)
            .from(ranking)
            .where(ranking.board.id.eq(content.get(content.size() - 1)
                .getBoardId()))
            .fetchOne();
        double cursorScore = getCursorScore(sort, cursorRanking);
        Long nextCursor = cursorRanking.getBoard()
            .getId();

        if (Objects.isNull(cursorInfo.targetId())) {
            Long boardCnt = queryFactory
                .select(board.countDistinct())
                .from(store)
                .join(board)
                .on(board.store.eq(store))
                .join(product)
                .on(product.board.eq(board))
                .where(filter)
                .fetchOne();

            if (Objects.isNull(boardCnt)) {
                boardCnt = 0L;
            }

            Long storeCnt = queryFactory
                .select(store.countDistinct())
                .from(store)
                .join(board)
                .on(board.store.eq(store))
                .join(product)
                .on(product.board.eq(board))
                .where(filter)
                .fetchOne();

            if (Objects.isNull(storeCnt)) {
                storeCnt = 0L;
            }

            return BoardCustomPage.from(content, nextCursor, cursorScore, hasNext, boardCnt,
                storeCnt);
        }
        return BoardCustomPage.from(content, nextCursor, cursorScore, hasNext);
    }

    private double getCursorScore(SortType sort, Ranking cursorRanking) {
        if (Objects.isNull(sort) || sort.equals(SortType.POPULAR)) {
            return cursorRanking.getPopularScore();
        }

        return cursorRanking.getRecommendScore();
    }

    private List<BoardResponseDto> getBoardResponseDtos(
        List<Board> boards,
        Map<Long, List<ProductTagDto>> productTagsByBoardId
    ) {
        List<BoardResponseDto> content = new ArrayList<>();
        for (int i = 0; i < boards.size(); i++) {
            if (i == PAGE_SIZE) {
                continue;
            }
            List<String> tags = addList(productTagsByBoardId.get(boards.get(i)
                .getId()));
            content.add(BoardResponseDto.from(boards.get(i), tags));
        }
        return content;
    }

    private boolean isHasNext(List<Board> boards) {
        return boards.size() >= PAGE_SIZE + 1;
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
                throw new BbangleException("Invalid SortType");
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
        FilterRequest filterRequest,
        QProduct product,
        QBoard board
    ) {

        BooleanBuilder filterBuilder = new BooleanBuilder();
        if (filterRequest.glutenFreeTag() != null) {
            filterBuilder.and(product.glutenFreeTag.eq(filterRequest.glutenFreeTag()));
        }
        if (filterRequest.highProteinTag() != null) {
            filterBuilder.and(product.highProteinTag.eq(filterRequest.highProteinTag()));
        }
        if (filterRequest.sugarFreeTag() != null) {
            filterBuilder.and(product.sugarFreeTag.eq(filterRequest.sugarFreeTag()));
        }
        if (filterRequest.veganTag() != null) {
            filterBuilder.and(product.veganTag.eq(filterRequest.veganTag()));
        }
        if (filterRequest.ketogenicTag() != null) {
            filterBuilder.and(product.ketogenicTag.eq(filterRequest.ketogenicTag()));
        }
        if (filterRequest.category() != null) {
            filterBuilder.and(product.category.eq(filterRequest.category()));
        }
        if (filterRequest.minPrice() != null) {
            filterBuilder.and(board.price.goe(filterRequest.minPrice()));
        }
        if (filterRequest.maxPrice() != null) {
            filterBuilder.and(board.price.loe(filterRequest.maxPrice()));
        }
        if (filterRequest.orderAvailableToday() != null && filterRequest.orderAvailableToday()) {
            DayOfWeek dayOfWeek = LocalDate.now()
                .getDayOfWeek();

            switch (dayOfWeek) {
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

}
