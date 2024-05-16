package com.bbangle.bbangle.store.repository;

import static java.util.Collections.emptySet;

import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.board.domain.QProduct;
import com.bbangle.bbangle.board.domain.TagEnum;
import com.bbangle.bbangle.board.dto.StoreAllBoardDto;
import com.bbangle.bbangle.board.dto.StoreBestBoardDto;
import com.bbangle.bbangle.board.repository.ProductRepository;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.page.StoreCustomPage;
import com.bbangle.bbangle.store.domain.QStore;
import com.bbangle.bbangle.store.dto.QStoreResponseDto;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import com.bbangle.bbangle.wishlist.domain.QWishListBoard;
import com.bbangle.bbangle.wishlist.domain.QWishListStore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreQueryDSLRepository {

    private static final Long PAGE_SIZE = 20L;
    private static final Long BEST_BOARD_PAGE_SIZE = 3L;
    private static final Long EMPTY_PAGE_CURSOR = -1L;

    private static final Boolean EMPTY_PAGE_HAS_NEXT = false;
    private static final QStore store = QStore.store;
    private static final QBoard board = QBoard.board;
    private static final QProduct product = QProduct.product;
    private static final QWishListStore wishListStore = QWishListStore.wishListStore;
    private static final QWishListBoard wishListBoard = QWishListBoard.wishListBoard;
    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Override
    public List<StoreBestBoardDto> findBestBoards(Long storeId) {
        List<Tuple> boards = queryFactory.select(
                board.id,
                board.profile,
                board.title,
                board.price,
                board.view
            )
            .from(board)
            .where(board.store.id.eq(storeId))
            .orderBy(board.view.desc())
            .limit(BEST_BOARD_PAGE_SIZE)
            .fetch();

        List<Long> boardIds = boards.stream()
            .map(tuple -> tuple.get(board.id))
            .toList();

        Map<Long, Set<Category>> categoryInfoByBoardId = productRepository
            .getCategoryInfoByBoardId(boardIds);

        return boards.stream()
            .map(boardWithStoreInfo -> {
                    Long boardId = boardWithStoreInfo.get(board.id);
                    Set<Category> categories = categoryInfoByBoardId.getOrDefault(boardId, emptySet());

                    return StoreBestBoardDto.builder()
                        .boardId(boardId)
                        .title(boardWithStoreInfo.get(board.title))
                        .thumbnail(boardWithStoreInfo.get(board.profile))
                        .isBundled(categories.size() > 1)
                        .price(boardWithStoreInfo.get(board.price))
                        .build();
                }
            ).toList();
    }

    @Override
    public SliceImpl<StoreAllBoardDto> getAllBoardWithLike(
        Pageable pageable,
        Long memberId,
        Long storeId
    ) {
        // 고유한 board.id를 선택하기 위한 서브쿼리 생성
        List<Long> boardSubQuery = queryFactory
            .select(board.id)
            .from(board)
            .where(board.store.id.eq(storeId))
            .orderBy(board.createdAt.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        // 메인 쿼리에서 서브쿼리를 사용하여 필요한 데이터를 조인하여 가져옴
        List<Tuple> fetch = queryFactory.select(
                board.id,
                board.profile,
                board.title,
                board.price,
                board.view,
                product.id,
                product.glutenFreeTag,
                product.highProteinTag,
                product.sugarFreeTag,
                product.veganTag,
                product.ketogenicTag,
                product.category
            )
            .from(product)
            .join(product.board, board)
            .leftJoin(wishListBoard)
            .on(wishListBoard.board.eq(board), wishListBoard.memberId.eq(memberId),
                wishListBoard.isDeleted.eq(false))
            .where(board.id.in(boardSubQuery))
            .fetch();

        List<StoreAllBoardDto> content = new ArrayList<>();

        // TagDto 초기화
        Set<String> allTag = new HashSet<>();
        List<String> tags = new ArrayList<>();
        Set<Category> categories = new HashSet<>();

        int resultSize = fetch.size();
        int index = 0;

        for (Tuple tuple : fetch) {
            index++;

            // 개별 태그의 True를 각각 확인하여 전체 태그로 구성
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
            allTag.addAll(tags);
            tags.clear();

            // ProductId가 달라지거나 반복문 마지막 일 시 Board 데이터 추가
            if (resultSize > index && tuple.get(board.id) != fetch.get(index)
                .get(board.id) || resultSize == index) {
                // 보드 리스트에 데이터 추가
                content.add(
                    StoreAllBoardDto.builder()
                        .boardId(tuple.get(board.id))
                        .thumbnail(tuple.get(board.profile))
                        .title(tuple.get(board.title))
                        .price(tuple.get(board.price))
                        .isWished(tuple.get(wishListBoard.id) != null ? true : false)
                        .isBundled(categories.size() > 1)
                        .tags(allTag.stream()
                            .toList())
                        .view(tuple.get(board.view))
                        .build());
                categories.clear();
                allTag.clear();
            }
        }

        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }


    @Override
    public SliceImpl<StoreAllBoardDto> getAllBoard(Pageable pageable, Long storeId) {
        // 고유한 board.id를 선택하기 위한 서브쿼리 생성
        List<Long> boardSubQuery = queryFactory
            .select(board.id)
            .from(board)
            .where(board.store.id.eq(storeId))
            .orderBy(board.createdAt.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        // 메인 쿼리에서 서브쿼리를 사용하여 필요한 데이터를 조인하여 가져옴
        List<Tuple> fetch = queryFactory.select(
                board.id,
                board.profile,
                board.title,
                board.price,
                board.view,
                product.id,
                product.glutenFreeTag,
                product.highProteinTag,
                product.sugarFreeTag,
                product.veganTag,
                product.ketogenicTag,
                product.category
            )
            .from(product)
            .join(product.board, board)
            .where(board.id.in(boardSubQuery))
            .fetch();

        List<StoreAllBoardDto> content = new ArrayList<>();

        // TagDto 초기화
        Set<String> allTag = new HashSet<>();
        List<String> tags = new ArrayList<>();
        Set<Category> categories = new HashSet<>();

        int resultSize = fetch.size();
        int index = 0;

        for (Tuple tuple : fetch) {
            index++;

            // 개별 태그의 True를 각각 확인하여 전체 태그로 구성
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
            allTag.addAll(tags);
            tags.clear();

            // ProductId가 달라지거나 반복문 마지막 일 시 Board 데이터 추가
            if (resultSize > index && tuple.get(board.id) != fetch.get(index)
                .get(board.id) || resultSize == index) {
                // 보드 리스트에 데이터 추가
                content.add(
                    StoreAllBoardDto.builder()
                        .boardId(tuple.get(board.id))
                        .thumbnail(tuple.get(board.profile))
                        .title(tuple.get(board.title))
                        .price(tuple.get(board.price))
                        .isWished(true)
                        .isBundled(categories.size() > 1)
                        .tags(allTag.stream()
                            .toList())
                        .view(tuple.get(board.view))
                        .build());
                categories.clear();
                allTag.clear();
            }
        }

        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<StoreAllBoardDto>(content, pageable, hasNext);
    }

    @Override
    public HashMap<Long, String> getAllStoreTitle() {
        List<Tuple> fetch = queryFactory
            .select(store.id, store.name)
            .from(store)
            .fetch();

        HashMap<Long, String> storeMap = new HashMap<>();
        fetch.forEach((tuple) -> storeMap.put(tuple.get(store.id), tuple.get(store.name)));

        return storeMap;
    }

    @Override
    public StoreCustomPage<List<StoreResponseDto>> getStoreList(Long cursorId, Long memberId) {
        BooleanBuilder cursorCondition = getCursorCondition(cursorId);
        List<StoreResponseDto> responseDtos = queryFactory.select(
                new QStoreResponseDto(
                    store.id,
                    store.name,
                    store.introduce,
                    store.profile
                )
            )
            .from(store)
            .where(cursorCondition)
            .limit(PAGE_SIZE + 1)
            .fetch();
        if (responseDtos.isEmpty()) {
            return StoreCustomPage.from(responseDtos, EMPTY_PAGE_CURSOR, EMPTY_PAGE_HAS_NEXT);
        }

        boolean hasNext = checkingHasNext(responseDtos);
        if (hasNext) {
            responseDtos.remove(responseDtos.get(responseDtos.size() - 1));
        }
        Long nextCursor = responseDtos.get(responseDtos.size() - 1).getStoreId();

        if (Objects.nonNull(memberId)) {
            findNextCursorPageWithLogin(responseDtos, memberId);
        }

        return StoreCustomPage.from(responseDtos, nextCursor, hasNext);
    }

    public List<StoreResponseDto> findNextCursorPageWithLogin(
        List<StoreResponseDto> cursorPage,
        Long memberId
    ) {
        List<Long> pageIds = getContentsIds(cursorPage);

        Member member = memberRepository.findMemberById(memberId);

        List<Long> wishedStore = queryFactory.select(
                wishListStore.store.id)
            .from(wishListStore)
            .where(wishListStore.member.eq(member)
                .and(wishListStore.isDeleted.eq(false))
                .and(wishListStore.store.id.in(pageIds)))
            .fetch();

        updateLikeStatus(wishedStore, cursorPage);

        return cursorPage;
    }

    private static List<Long> getContentsIds(List<StoreResponseDto> cursorPage) {
        return cursorPage
            .stream()
            .map(StoreResponseDto::getStoreId)
            .toList();
    }

    private static void updateLikeStatus(
        List<Long> wishedIds,
        List<StoreResponseDto> cursorPage
    ) {
        for (Long id : wishedIds) {
            for (StoreResponseDto response : cursorPage) {
                if (id.equals(response.getStoreId())) {
                    response.isWishStore();
                }
            }
        }
    }

    private static boolean checkingHasNext(List<StoreResponseDto> responseDtos) {
        return responseDtos.size() >= PAGE_SIZE + 1;
    }

    private BooleanBuilder getCursorCondition(Long cursorId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Objects.isNull(cursorId)) {
            return booleanBuilder;
        }
        Long startId = checkingBoardExistence(cursorId);

        booleanBuilder.and(store.id.goe(startId));
        return booleanBuilder;
    }

    private Long checkingBoardExistence(Long cursorId) {
        Long checkingId = queryFactory.select(store.id)
            .from(store)
            .where(store.id.eq(cursorId))
            .fetchOne();

        if (Objects.isNull(checkingId)) {
            throw new IllegalArgumentException("존재하지 않는 게시글 아이디입니다.");
        }

        return cursorId + 1;
    }

}


