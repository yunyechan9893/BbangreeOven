package com.bbangle.bbangle.store.service;

import com.bbangle.bbangle.page.StoreDetailCustomPage;
import com.bbangle.bbangle.store.dto.PopularBoardResponse;
import com.bbangle.bbangle.store.dto.StoreBoardsResponse;

import com.bbangle.bbangle.page.StoreCustomPage;
import com.bbangle.bbangle.store.dto.StoreResponse;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import com.bbangle.bbangle.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreResponse getStoreResponse(Long memberId, Long storeId){
        return storeRepository.getStoreResponse(memberId, storeId);
    }

    public List<PopularBoardResponse> getPopularBoardResponses(Long memberId, Long storeId) {
        return storeRepository.getPopularBoardResponses(memberId, storeId);
    }

    public StoreDetailCustomPage<List<StoreBoardsResponse>> getStoreAllBoard(Long memberId, Long storeId,
        Long boardIdAsCursorId) {
        return storeRepository.getStoreBoardsResponse(memberId, storeId, boardIdAsCursorId);
    }

    public StoreCustomPage<List<StoreResponseDto>> getList(Long cursorId, Long memberId) {
        return storeRepository.getStoreList(cursorId, memberId);
    }
}