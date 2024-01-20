package com.bbangle.bbangle.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.bbangle.bbangle.dto.BoardAvailableDayDto;
import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardDto;
import com.bbangle.bbangle.dto.BoardImgDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.dto.ProductDto;
import com.bbangle.bbangle.dto.ProductTagDto;
import com.bbangle.bbangle.dto.StoreDto;
import com.bbangle.bbangle.exception.CategoryTypeException;
import com.bbangle.bbangle.model.Category;
import com.bbangle.bbangle.model.QBoard;
import com.bbangle.bbangle.model.QProduct;
import com.bbangle.bbangle.model.QProductImg;
import com.bbangle.bbangle.model.QStore;
import com.bbangle.bbangle.model.TagEnum;
import com.bbangle.bbangle.repository.BoardQueryDSLRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Slice<BoardResponseDto> getBoardResponseDto(String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                                       Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                                       String category, Integer minPrice, Integer maxPrice,
                                                       Pageable pageable) {

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
                board);

// Step 1: 페이징을 위한 서브쿼리
        List<Long> pagedBoardIds = queryFactory
            .select(board.id)
            .from(board)
            .where(filter)
            .orderBy(board.createdAt.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

// Step 2: 메인 쿼리
        List<Tuple> fetch = queryFactory
            .select(
                board.id,
                store.id,
                store.name,
                board.profile,
                board.title,
                board.price,
                product.glutenFreeTag,
                product.highProteinTag,
                product.sugarFreeTag,
                product.veganTag,
                product.ketogenicTag)
            .from(board)
            .leftJoin(product).on(product.board.eq(board))
            .leftJoin(store).on(board.store.eq(store))
            .where(board.id.in(pagedBoardIds))
            .fetch();

        Map<Long, List<ProductTagDto>> productTagsByBoardId = fetch.stream()
            .collect(Collectors.groupingBy(
                tuple -> tuple.get(board.id),
                Collectors.mapping(tuple -> ProductTagDto.builder()
                        .glutenFreeTag(Boolean.TRUE.equals(tuple.get(product.glutenFreeTag)))
                        .highProteinTag(Boolean.TRUE.equals(tuple.get(product.highProteinTag)))
                        .sugarFreeTag(Boolean.TRUE.equals(tuple.get(product.sugarFreeTag)))
                        .veganTag(Boolean.TRUE.equals(tuple.get(product.veganTag)))
                        .ketogenicTag(Boolean.TRUE.equals(tuple.get(product.ketogenicTag)))
                        .build(),
                    Collectors.toList())
            ));

        Set<Long> boardIds = new HashSet<>();

        List<BoardResponseDto> content = new ArrayList<>();

        for (Tuple tuple : fetch) {
            Long currentBoardId = tuple.get(board.id);
            if (!boardIds.contains(currentBoardId)) {
                boardIds.add(currentBoardId);

                // 결과를 DTO로 변환
                content.add(BoardResponseDto.builder()
                    .boardId(tuple.get(board.id))
                    .storeId(tuple.get(store.id))
                    .storeName(tuple.get(store.name))
                    .thumbnail(tuple.get(board.profile))
                    .title(tuple.get(board.title))
                    .price(tuple.get(board.price))
                    .isWished(true) // 이 값은 필요에 따라 설정
                    .tags(addList(productTagsByBoardId.get(tuple.get(board.id))))
                    .build());
            }
        }

        // 다음 페이지 존재 여부 확인
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            // 마지막 항목 제거
            content.remove(content.size() - 1);
        }

        // Slice 객체 반환
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private static BooleanBuilder setFilteringCondition(Boolean glutenFreeTag, Boolean highProteinTag,
                                                        Boolean sugarFreeTag,
                                                        Boolean veganTag, Boolean ketogenicTag, String category,
                                                        Integer minPrice, Integer maxPrice,
                                                        QProduct product, QBoard board) {
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
        return filterBuilder;
    }

    private List<String> addList(List<ProductTagDto> dtos) {
        boolean glutenFreeTag = false;
        boolean highProteinTag = false;
        boolean sugarFreeTag = false;
        boolean veganTag = false;
        boolean ketogenicTag = false;
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
        List<String> tags = new ArrayList<>();
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

    private HashMap<String, Boolean> getTagHash(String tagName, Boolean isTrued) {
        HashMap<String, Boolean> tagHash = new HashMap<>();
        tagHash.put(tagName, isTrued);
        return tagHash;
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
                product.glutenFreeTag,
                product.highProteinTag,
                product.sugarFreeTag,
                product.veganTag,
                product.ketogenicTag)
            .from(product)
            .join(product.board, board)
            .join(board.store, store)
            .leftJoin(productImg).on(board.id.eq(productImg.board.id))
            .where(board.id.eq(boardId))
            .fetch();

        StoreDto storeDto = StoreDto.builder().build();
        BoardDto boardDto = BoardDto.builder().build();
        List<ProductDto> productDtos = new ArrayList<>();
        List<BoardImgDto> boardImgDtos = new ArrayList<>();

        int resultSize = fetch.size();
        int index = 0;

        List<Long> alreadyProductImgId = new ArrayList<>();
        List<Long> alreadyProductId = new ArrayList<>();

        for (Tuple tuple : fetch) {
            index++;

            List<String> tags = new ArrayList<>();
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

            // 중복 제거 및 상품 추가
            if (alreadyProductId.indexOf(tuple.get(product.id)) == -1) {
                productDtos.add(
                    ProductDto.builder()
                        .title(tuple.get(product.title))
                        .tags(tags).build());

                alreadyProductId.add(tuple.get(product.id));
            }

            // 중복 제거 및 보드 이미지 추가
            if (alreadyProductImgId.indexOf(tuple.get(productImg.id)) == -1 && tuple.get(productImg.id) != null) {
                boardImgDtos.add(
                    BoardImgDto.builder()
                        .id(tuple.get(productImg.id))
                        .url(tuple.get(productImg.url))
                        .build()
                );

                alreadyProductImgId.add(tuple.get(productImg.id));
            }

            // 반복문 마지막 때 보드 및 스토어 추가
            if (index == resultSize) {
                boardDto = BoardDto.builder()
                    .id(tuple.get(board.id))
                    .profile(tuple.get(board.profile))
                    .images(boardImgDtos)
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
                    .isWished(true)
                    .isBundled(false)
                    .detail(tuple.get(board.detail))
                    .products(productDtos)
                    .build();

                storeDto = StoreDto.builder()
                    .id(tuple.get(store.id))
                    .name(tuple.get(store.name))
                    .profile(tuple.get(store.profile))
                    .isWished(true)
                    .build();
            }
        }


        return BoardDetailResponseDto.builder()
            .store(storeDto)
            .board(boardDto)
            .build();
    }

}
