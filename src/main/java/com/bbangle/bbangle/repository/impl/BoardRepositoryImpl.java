package com.bbangle.bbangle.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.dto.ProductTagDto;
import com.bbangle.bbangle.model.*;
import com.bbangle.bbangle.repository.BoardQueryDSLRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardResponseDto> getBoardResponseDto() {

        QBoard board = QBoard.board;
        QProduct product = QProduct.product;
        QStore store = QStore.store;

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
                .from(product)
                .join(product.board, board)
                .join(board.store, store)
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

        return fetch.stream()
                .map(tuple -> BoardResponseDto.builder()
                        .boardId(tuple.get(board.id))
                        .storeId(tuple.get(store.id))
                        .storeName(tuple.get(store.name))
                        .thumbnail(tuple.get(board.profile))
                        .title(tuple.get(board.title))
                        .price(tuple.get(board.price))
                        .tagDto(checkingTrue(productTagsByBoardId.get(tuple.get(board.id))))
                        .build())
                .distinct() // 중복 제거
                .toList();
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

    @Override
    public Boolean getBoardDetailResponseDto(Long boardId) {
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
                        product.title,
                        product.glutenFreeTag,
                        product.highProteinTag,
                        product.sugarFreeTag,
                        product.veganTag,
                        product.ketogenicTag)
                .from(board)
                .join(board.store, store)
                .leftJoin(product).on(board.id.eq(product.id))
                .leftJoin(productImg).on(board.id.eq(productImg.id))
                .where(board.id.eq(boardId))
                .fetch();



//        Map<Long, List<ProductTagDto>> productTagsByBoardId = fetch.stream()
//                .collect(Collectors.groupingBy(
//                        tuple -> tuple.get(board.id),
//                        Collectors.mapping(tuple -> ProductTagDto.builder()
//                                        .glutenFreeTag(tuple.get(product.glutenFreeTag))
//                                        .highProteinTag(tuple.get(product.highProteinTag))
//                                        .sugarFreeTag(tuple.get(product.sugarFreeTag))
//                                        .veganTag(tuple.get(product.veganTag))
//                                        .ketogenicTag(tuple.get(product.ketogenicTag))
//                                        .build(),
//                                Collectors.toList())
//                ));
        Map<Object, List<Tuple>> collect = fetch.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(board.title)));

        System.out.println(collect);


        return false;
//        Map<Long, List<ProductImg>> productImageByBoardId = fetch.stream()
//                .collect(Collectors.groupingBy(
//                        tuple -> tuple.get(board.id),
//                        Collectors.mapping(tuple -> ProductDto.builder()
//
//                                        .build(),
//                                Collectors.toList())
//                ));
//
//        fetch.stream()
//                .map(tuple -> BoardDetailResponseDto.builder()
//                        .boardDto()
//                        .storeDto()
//                        .build())
//                .distinct() // 중복 제거
//                .toList();

//        return BoardDetailResponseDto.builder()
//                .boardDto()
//                .storeDto()
//                .build();
    }
}