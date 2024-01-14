package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.dto.*;
import com.bbangle.bbangle.model.QBoard;
import com.bbangle.bbangle.model.QProduct;
import com.bbangle.bbangle.model.QStore;
import com.bbangle.bbangle.model.TagEnum;
import com.bbangle.bbangle.repository.StoreQueryDSLRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;


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

        StoreDto storeDto = StoreDto.builder().build();
        List<BoardDto> boardDtos = new ArrayList<>();

        // TagDto 초기화
        TagDto tagDto = TagDto.builder()
                .glutenFreeTag(getTagHash(TagEnum.GLUTEN_FREE.label(), false))
                .highProteinTag(getTagHash(TagEnum.HIGH_PROTEIN.label(), false))
                .sugarFreeTag(getTagHash(TagEnum.SUGAR_FREE.label(), false))
                .veganTag(getTagHash(TagEnum.VEGAN.label(), false))
                .ketogenicTag(getTagHash(TagEnum.KETOGENIC.label(), false))
                .build();

        int resultSize = fetch.size();
        Long curProductId = fetch.get(0).get(board.id);
        int index = 0;

        for (Tuple tuple:
        fetch) {
            index++;

            // 개별 태그의 True를 각각 확인하여 전체 태그로 구성
            if (!tagDto.glutenFreeTag().get(TagEnum.GLUTEN_FREE.label()) && tuple.get(product.glutenFreeTag))
                tagDto.glutenFreeTag().put(TagEnum.GLUTEN_FREE.label(), true);

            if (!tagDto.highProteinTag().get(TagEnum.HIGH_PROTEIN.label()) && tuple.get(product.highProteinTag))
                tagDto.highProteinTag().put(TagEnum.HIGH_PROTEIN.label(), true);

            if (!tagDto.sugarFreeTag().get(TagEnum.SUGAR_FREE.label()) && tuple.get(product.sugarFreeTag))
                tagDto.sugarFreeTag().put(TagEnum.SUGAR_FREE.label(), true);

            if (!tagDto.veganTag().get(TagEnum.VEGAN.label()) && tuple.get(product.veganTag))
                tagDto.veganTag().put(TagEnum.VEGAN.label(), true);

            if (!tagDto.ketogenicTag().get(TagEnum.KETOGENIC.label()) && tuple.get(product.ketogenicTag))
                tagDto.ketogenicTag().put(TagEnum.KETOGENIC.label(), true);

            // ProductId가 달라지거나 반복문 마지막 일 시 Board 데이터 추가
            if (tuple.get(board.id) != curProductId || index == resultSize){
                // 현재 상품 아이디를 최신 상품 아이디로 변경
                curProductId = tuple.get(board.id);

                // 보드 리스트에 데이터 추가
                boardDtos.add(BoardDto.builder()
                        .id(tuple.get(board.id))
                        .profile(tuple.get(board.profile))
                        .title(tuple.get(board.title))
                        .price(tuple.get(board.price))
                        .isWished(true)
                        .isBundled(false)
                        .tags(tagDto)
                        .build());

                // 태그 초기화
                tagDto = TagDto.builder()
                        .glutenFreeTag(getTagHash(TagEnum.GLUTEN_FREE.label(), false))
                        .highProteinTag(getTagHash(TagEnum.HIGH_PROTEIN.label(), false))
                        .sugarFreeTag(getTagHash(TagEnum.SUGAR_FREE.label(), false))
                        .veganTag(getTagHash(TagEnum.VEGAN.label(), false))
                        .ketogenicTag(getTagHash(TagEnum.KETOGENIC.label(), false))
                        .build();
            }

            // 반복문 마지막에 스토어 Dto 추가
            if (index == resultSize){
                storeDto = StoreDto.builder()
                        .id(tuple.get(store.id))
                        .profile(tuple.get(store.profile))
                        .name(tuple.get(store.name))
                        .introduce(tuple.get(store.introduce))
                        .isWished(true)
                        .build();
            }
        }

        // 인기순을 찾기 위해 View를 기준으로 내림차순 정렬
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

        return StoreDetailResponseDto.builder()
                .store(storeDto)
                .bestProducts(bestBoards)
                .allProducts(boardDtos)
                .build();
    }

    private HashMap<String, Boolean> getTagHash(String tagName, Boolean isTrued){
        HashMap<String, Boolean> tagHash = new HashMap<>();
        tagHash.put(tagName, isTrued);
        return tagHash;
    }
}


