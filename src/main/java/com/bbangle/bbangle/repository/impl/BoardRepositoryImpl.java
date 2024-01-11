package com.bbangle.bbangle.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bbangle.bbangle.dto.*;
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
                .leftJoin(product).on(board.id.eq(product.board.id))
                .leftJoin(productImg).on(board.id.eq(productImg.board.id))
                .where(board.id.eq(boardId))
                .fetch();


        List<ProductDto> products = fetch.stream()
                .map(tuple ->
                        ProductDto.builder()
                                        .name(tuple.get(product.title))
                                        .tagDto(
                                                ProductTagDto.builder()
                                                        .glutenFreeTag(tuple.get(product.glutenFreeTag))
                                                        .highProteinTag(tuple.get(product.highProteinTag))
                                                        .sugarFreeTag(tuple.get(product.sugarFreeTag))
                                                        .veganTag(tuple.get(product.veganTag))
                                                        .ketogenicTag(tuple.get(product.ketogenicTag))
                                                        .build()
                                        )
                                        .build()
                ).collect(Collectors.toList());

        List<String> boardImgs = fetch.stream()
                .map(tuple ->
                        getImgUrl(
                                BoardImgDto.builder()
                                        .url(tuple.get(productImg.url))
                                        .build()
                        )
                ).collect(Collectors.toList());

        List<StoreDto> storeDtos = fetch.stream()
                .map(tuple ->
                        StoreDto.builder()
                                .id(tuple.get(board.id))
                                .name(tuple.get(board.title))
                                .profile(tuple.get(board.profile))
                                .isWished(true)
                                .build()
                ).collect(Collectors.toList());

        StoreDto singleStoreDto = storeDtos.isEmpty() ? null : storeDtos.get(0);


        List<BoardDto> boardDtos = fetch.stream()
                .map(tuple ->
                        BoardDto.builder()
                                .id(tuple.get(board.id))
                                .profile(tuple.get(board.profile))
                                .images(boardImgs)
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
                                .products(products)
                                .build()
                )
                .collect(Collectors.toList());

        BoardDto singleBoardDto = boardDtos.isEmpty() ? null : boardDtos.get(0);

        System.out.println(BoardDetailResponseDto.builder()
                .storeDto(singleStoreDto)
                .boardDto(singleBoardDto)
                .build());


        return BoardDetailResponseDto.builder()
                .storeDto(singleStoreDto)
                .boardDto(singleBoardDto)
                .build();
    }

    private String getImgUrl(BoardImgDto boardImgDto){
        return boardImgDto.url().toString();
    }
}