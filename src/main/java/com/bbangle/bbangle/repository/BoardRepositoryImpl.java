package com.bbangle.bbangle.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.dto.ProductTagDto;
import com.bbangle.bbangle.model.Category;
import com.bbangle.bbangle.model.QBoard;
import com.bbangle.bbangle.model.QProduct;
import com.bbangle.bbangle.model.QStore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardResponseDto> getBoardResponseDto(String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                                      Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                                      String category) {

        QBoard board = QBoard.board;
        QProduct product = QProduct.product;
        QStore store = QStore.store;

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
            filterBuilder.and(product.category.eq(Category.valueOf(category)));
        }

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
            .where(filterBuilder)
            .orderBy(board.createdAt.asc())
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

        return getList(fetch, board, store, productTagsByBoardId);
    }


    private List<BoardResponseDto> getList(List<Tuple> fetch, QBoard board, QStore store,
                                           Map<Long, List<ProductTagDto>> productTagsByBoardId) {
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

}
