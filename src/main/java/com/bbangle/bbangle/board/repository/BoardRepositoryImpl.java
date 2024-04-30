package com.bbangle.bbangle.board.repository;

import static com.bbangle.bbangle.wishList.domain.QWishlistProduct.wishlistProduct;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.domain.Product;
import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.board.domain.QBoardDetail;
import com.bbangle.bbangle.board.domain.QProduct;
import com.bbangle.bbangle.board.domain.QProductImg;
import com.bbangle.bbangle.board.domain.TagEnum;
import com.bbangle.bbangle.board.dto.BoardAvailableDayDto;
import com.bbangle.bbangle.board.dto.BoardDetailDto;
import com.bbangle.bbangle.board.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.board.dto.BoardImgDto;
import com.bbangle.bbangle.board.dto.BoardResponseDto;
import com.bbangle.bbangle.board.dto.CursorInfo;
import com.bbangle.bbangle.board.dto.FilterRequest;
import com.bbangle.bbangle.board.dto.ProductDto;
import com.bbangle.bbangle.board.dto.QDetailResponseDto;
import com.bbangle.bbangle.board.repository.query.BoardQueryProviderResolver;
import com.bbangle.bbangle.common.sort.SortType;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.page.BoardCustomPage;
import com.bbangle.bbangle.ranking.domain.QRanking;
import com.bbangle.bbangle.ranking.domain.Ranking;
import com.bbangle.bbangle.store.domain.QStore;
import com.bbangle.bbangle.store.dto.StoreDto;
import com.bbangle.bbangle.wishList.domain.QWishlistFolder;
import com.bbangle.bbangle.wishList.domain.QWishlistProduct;
import com.bbangle.bbangle.wishList.domain.QWishlistStore;
import com.bbangle.bbangle.wishList.domain.WishlistFolder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
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

    public static final int BOARD_PAGE_SIZE = 10;

    private final BoardQueryProviderResolver boardQueryProviderResolver;
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
        BooleanBuilder filter = new BoardFilterCreator(filterRequest).create();

        List<Board> boards = boardQueryProviderResolver.resolve(sort, cursorInfo)
            .findBoards(filter);

        // FIXME: 요 아래부분은 service 에서 해야되지않나 싶은 부분... 레파지토리의 역할은 board 리스트 넘겨주는곳 까지가 아닐까 싶어서요
        List<BoardResponseDto> content = convertToBoardResponse(boards);
        return getBoardCustomPage(sort, cursorInfo, filter, content, isHasNext(boards));
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

        boolean hasNext = boards.size() > pageable.getPageSize();
        List<BoardResponseDto> content = boards.stream()
            .limit(pageable.getPageSize())
            .map(board -> BoardResponseDto.inFolder(board, extractTags(board.getProductList())))
            .toList();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public BoardDetailResponseDto getBoardDetailResponse(Long memberId, Long boardId) {
        // FIXME: 리팩토링 개선필요... 코드 너무 보기 힘듭니다
        List<Expression<?>> columns = new ArrayList<>();
        columns.add(store.id);
        columns.add(store.name);
        columns.add(store.profile);
        columns.add(board.id);
        columns.add(board.profile);
        columns.add(board.title);
        columns.add(board.price);
        columns.add(productImg.id);
        columns.add(productImg.url);
        columns.add(board.monday);
        columns.add(board.tuesday);
        columns.add(board.wednesday);
        columns.add(board.thursday);
        columns.add(board.friday);
        columns.add(board.saturday);
        columns.add(board.sunday);
        columns.add(board.purchaseUrl);
        columns.add(product.id);
        columns.add(product.title);
        columns.add(product.category);
        columns.add(product.glutenFreeTag);
        columns.add(product.highProteinTag);
        columns.add(product.sugarFreeTag);
        columns.add(product.veganTag);
        columns.add(product.ketogenicTag);

        // 회원이라면 위시리스트 등록 여부도 파악
        if (memberId != null && memberId > 0) {
            columns.add(wishlistProduct.id);
        }

        var jpaQuery = queryFactory
            .select(columns.toArray(new Expression[0]))
            .from(product)
            .where(board.id.eq(boardId))
            .join(product.board, board)
            .join(board.store, store)
            .leftJoin(productImg)
            .on(board.id.eq(productImg.board.id));

        if (memberId != null && memberId > 0) {
            jpaQuery.leftJoin(wishlistProduct)
                .on(wishlistProduct.board.eq(board), wishlistProduct.memberId.eq(memberId),
                    wishlistProduct.isDeleted.eq(false))
                .leftJoin(wishlistStore)
                .on(wishlistStore.store.eq(store), wishlistStore.member.id.eq(memberId),
                    wishlistStore.isDeleted.eq(false));
            columns.add(wishlistProduct.id);
        }

        var fetch = jpaQuery.fetch();

        var boardDetails = queryFactory.select(new QDetailResponseDto(
                boardDetail.id,
                boardDetail.imgIndex,
                boardDetail.url
            ))
            .from(boardDetail)
            .where(board.id.eq(boardId))
            .stream()
            .toList();

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
                    .detail(boardDetails)
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
    public List<Long> getLikedContentsIds(List<Long> responseList, Long memberId) {
        return queryFactory.select(board.id)
            .from(board)
            .leftJoin(wishlistProduct)
            .on(board.eq(wishlistProduct.board))
            .where(board.id.in(responseList)
                .and(wishlistProduct.memberId.eq(memberId))
                .and(wishlistProduct.isDeleted.eq(false)))
            .fetch();
    }

    private BoardCustomPage<List<BoardResponseDto>> getBoardCustomPage(
        SortType sort,
        CursorInfo cursorInfo,
        BooleanBuilder filter,
        List<BoardResponseDto> content,
        boolean hasNext
    ) {
        if (content.isEmpty()) {
            return BoardCustomPage.emptyPage();
        }

        Long boardCursor = content.get(content.size() - 1).getBoardId();
        Double cursorScore = queryFactory
            .select(getScoreColumnBySortType(sort))
            .from(ranking)
            .join(board)
            .on(ranking.board.eq(board))
            .fetchJoin()
            .where(ranking.board.id.eq(boardCursor))
            .fetchFirst();

        if (Objects.isNull(cursorInfo.targetId())) {
            // FIXME: count 쿼리 분리 필요
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

            return BoardCustomPage.from(content, boardCursor, cursorScore, hasNext, boardCnt,
                storeCnt);
        }
        return BoardCustomPage.from(content, boardCursor, cursorScore, hasNext);
    }

    private NumberPath<Double> getScoreColumnBySortType(SortType sort) {
        return SortType.POPULAR.equals(sort) ? ranking.popularScore : ranking.recommendScore;
    }

    private double getCursorScore(SortType sort, Ranking cursorRanking) {
        if (Objects.isNull(sort) || sort.equals(SortType.POPULAR)) {
            return cursorRanking.getPopularScore();
        }

        return cursorRanking.getRecommendScore();
    }

    private boolean isHasNext(List<Board> boards) {
        return boards.size() >= BOARD_PAGE_SIZE + 1;
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
        orderSpecifier = switch (SortType.fromString(sort)) {
            case RECENT -> products.createdAt.desc();
            case LOW_PRICE -> board.price.asc();
            case POPULAR -> board.wishCnt.desc();
            default -> throw new BbangleException("Invalid SortType");
        };
        return orderSpecifier;
    }

    private List<BoardResponseDto> convertToBoardResponse(List<Board> boards) {
        Map<Long, List<String>> tagMapByBoardId = boards.stream()
            .collect(Collectors.toMap(
                Board::getId,
                board -> extractTags(board.getProductList())
            ));

        return boards.stream()
            .limit(BOARD_PAGE_SIZE)
            .map(board -> BoardResponseDto.from(board, tagMapByBoardId.get(board.getId())))
            .toList();
    }

    private List<String> extractTags(List<Product> products) {
        if (products == null) {
            return Collections.emptyList();
        }

        HashSet<String> tags = new HashSet<>();
        for (Product dto : products) {
            addTagIfTrue(tags, dto.isGlutenFreeTag(), TagEnum.GLUTEN_FREE.label());
            addTagIfTrue(tags, dto.isHighProteinTag(), TagEnum.HIGH_PROTEIN.label());
            addTagIfTrue(tags, dto.isSugarFreeTag(), TagEnum.SUGAR_FREE.label());
            addTagIfTrue(tags, dto.isVeganTag(), TagEnum.VEGAN.label());
            addTagIfTrue(tags, dto.isKetogenicTag(), TagEnum.KETOGENIC.label());
        }
        return new ArrayList<>(tags);
    }

    private void addTagIfTrue(Set<String> tags, boolean condition, String tag) {
        if (condition) tags.add(tag);
    }
}
