package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.dto.*;
import com.bbangle.bbangle.model.*;
import com.bbangle.bbangle.repository.queryDsl.StoreQueryDSLRepository;
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
                product.id,
                product.glutenFreeTag,
                product.highProteinTag,
                product.sugarFreeTag,
                product.veganTag,
                product.ketogenicTag,
                product.category,
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
        Set<String> allTag = new HashSet<>();
        List<String> tags = new ArrayList<>();
        Set<Category> categories = new HashSet<>();

        int resultSize = fetch.size();
        int index = 0;

        for (Tuple tuple: fetch) {
            index++;

            // 개별 태그의 True를 각각 확인하여 전체 태그로 구성
            if (tuple.get(product.glutenFreeTag))  tags.add(TagEnum.GLUTEN_FREE.label());
            if (tuple.get(product.highProteinTag)) tags.add(TagEnum.HIGH_PROTEIN.label());
            if (tuple.get(product.sugarFreeTag))   tags.add(TagEnum.SUGAR_FREE.label());
            if (tuple.get(product.veganTag))       tags.add(TagEnum.VEGAN.label());
            if (tuple.get(product.ketogenicTag))   tags.add(TagEnum.KETOGENIC.label());
            categories.add(tuple.get(product.category));
            allTag.addAll(tags);
            tags.clear();

            // ProductId가 달라지거나 반복문 마지막 일 시 Board 데이터 추가
            if ( resultSize > index &&  tuple.get(board.id) != fetch.get(index).get(board.id) || resultSize == index){
                // 보드 리스트에 데이터 추가
                boardDtos.add(BoardDto.builder()
                        .boardId(tuple.get(board.id))
                        .thumbnail(tuple.get(board.profile))
                        .title(tuple.get(board.title))
                        .price(tuple.get(board.price))
                        .isWished(true)
                        .isBundled(categories.size() > 1)
                        .tags(allTag.stream().toList())
                        .view(tuple.get(board.view))
                        .build());
                categories.clear();
                allTag.clear();
            }

            // 반복문 마지막에 스토어 Dto 추가
            if (index == resultSize){
                storeDto = StoreDto.builder()
                        .storeId(tuple.get(store.id))
                        .profile(tuple.get(store.profile))
                        .storeName(tuple.get(store.name))
                        .introduce(tuple.get(store.introduce).isBlank() ? "": tuple.get(store.introduce))
                        .isWished(true)
                        .build();
            }
        }

        List<BoardDto> allProducts = new ArrayList<>(boardDtos);

        // 인기순을 찾기 위해 View를 기준으로 내림차순 정렬
        Collections.sort(boardDtos, (t1, t2) -> Integer.compare(t2.view(), t1.view()));

        int toIndex = Math.min(boardDtos.size(), 3);
        return StoreDetailResponseDto.builder()
                .store(storeDto)
                .bestProducts(boardDtos.subList(0, toIndex))
                .allProducts(allProducts)
                .build();
    }

    @Override
    public HashMap<Long, String> getAllStoreTitle() {
        QStore store = QStore.store;

        List<Tuple> fetch = jpaQueryFactory
                .select(store.id, store.name)
                .from(store)
                .fetch();

        HashMap<Long, String> storeMap = new HashMap<>();
        fetch.forEach((tuple) -> storeMap.put(tuple.get(store.id), tuple.get(store.name)));

        return storeMap;
    }

}


