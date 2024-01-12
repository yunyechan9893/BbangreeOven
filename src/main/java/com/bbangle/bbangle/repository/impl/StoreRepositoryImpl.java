package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.dto.*;
import com.bbangle.bbangle.model.QBoard;
import com.bbangle.bbangle.model.QProduct;
import com.bbangle.bbangle.model.QStore;
import com.bbangle.bbangle.repository.StoreQueryDSLRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreQueryDSLRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public StoreDetailResponseDto getStoreDetailResponseDto(Long storeId) {
        QStore store = QStore.store;
        QBoard board = QBoard.board;
        QProduct product = QProduct.product;

        List<Tuple> fetch = jpaQueryFactory.select(
                store.id,
                store.profile,
                store.name,
                store.introduce,
                board.id,
                board.profile,
                board.title,
                board.price,
                product.glutenFreeTag,
                product.highProteinTag,
                product.sugarFreeTag,
                product.veganTag,
                product.ketogenicTag,
                board.view
        ).from(product)
                .join(product.board, board)
                .join(board.store, store)
                .where(board.store.id.eq(storeId))
                .orderBy(board.createdAt.desc())
                .fetch();

        Map<Long, List<ProductTagDto>> productTagsByBoardId = fetch.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(board.id),
                        Collectors.mapping(tuple -> ProductTagDto.builder()
                                        .glutenFreeTag(tuple.get(product.glutenFreeTag))
                                        .highProteinTag(tuple.get(product.highProteinTag))
                                        .sugarFreeTag(tuple.get(product.sugarFreeTag))
                                        .veganTag(tuple.get(product.veganTag))
                                        .ketogenicTag(tuple.get(product.ketogenicTag))
                                        .build(),
                                Collectors.toList())
                ));

        List<BoardDto> boardDtos = fetch.stream()
                .map(tuple ->
                        BoardDto.builder()
                                .id(tuple.get(board.id))
                                .profile(tuple.get(board.profile))
                                .title(tuple.get(board.title))
                                .price(tuple.get(board.price))
                                .isWished(true)
                                .isBundled(false)
                                .allTags(checkingTrue(productTagsByBoardId.get(tuple.get(board.id))))
                                .build()

                )
                .distinct()
                .collect(Collectors.toList());




        List<StoreDto> storeDtos = fetch.stream()
                .map(tuple ->
                        StoreDto.builder()
                                .id(tuple.get(store.id))
                                .profile(tuple.get(store.profile))
                                .name(tuple.get(store.name))
                                .introduce(tuple.get(store.introduce))
                                .isWished(true)
                                .build()
                ).collect(Collectors.toList());

        StoreDto singleStoreDto = storeDtos.isEmpty() ? null : storeDtos.get(0);

        Collections.sort(fetch, (t1, t2) -> {
            // 내림차순으로 정렬하려는 값 선택 (여기서는 value1을 선택)
            return Integer.compare(t2.get(board.view), t1.get(board.view));
        });


        List<BoardDto> bestBoards = fetch.stream()
                .map(
                tuple -> BoardDto.builder()
                        .id(tuple.get(board.id))
                        .profile(tuple.get(board.profile))
                        .title(tuple.get(board.title))
                        .price(tuple.get(board.price))
                        .isBundled(false)
                        .build()
        )
                .distinct()
                .limit(3)
                .toList();


        // 이따 singleStoreDto, boardDtos 리턴하기
        return StoreDetailResponseDto.builder()
                .storeDto(singleStoreDto)
                .bestProducts(bestBoards)
                .allProducts(boardDtos)
                .build();
    }

    private ProductTagDto checkingTrue(List<ProductTagDto> dtos) {
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
        return ProductTagDto
                .builder()
                .glutenFreeTag(glutenFreeTag)
                .highProteinTag(highProteinTag)
                .sugarFreeTag(sugarFreeTag)
                .veganTag(veganTag)
                .ketogenicTag(ketogenicTag)
                .build();
    }
}


