package com.bbangle.bbangle.store.service;

import com.bbangle.bbangle.board.dto.StoreBestBoardDto;
import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;

import com.bbangle.bbangle.board.dto.StoreAllBoardDto;

import com.bbangle.bbangle.page.StoreCustomPage;
import com.bbangle.bbangle.store.domain.Store;
import com.bbangle.bbangle.store.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.store.dto.StoreDto;
import com.bbangle.bbangle.store.dto.StoreDto.StoreDtoBuilder;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import com.bbangle.bbangle.store.repository.StoreRepository;
import com.bbangle.bbangle.wishlist.repository.WishListStoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private static final int PAGE_SIZE = 10;
    private final StoreRepository storeRepository;
    private final WishListStoreRepository wishListStoreRepository;

    public StoreDetailResponseDto getStoreDetailResponse(Long memberId, Long storeId) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new BbangleException(BbangleErrorCode.STORE_NOT_FOUND));

        StoreDto storeDto = createStoreDto(memberId, store);
        List<StoreBestBoardDto> bestBoards = storeRepository.findBestBoards(storeId);

        return StoreDetailResponseDto.builder()
            .store(storeDto)
            .bestProducts(bestBoards)
            .build();
    }

    private StoreDto createStoreDto(Long memberId, Store store) {
        boolean isWished = getIsWished(memberId, store.getId());
        return StoreDto.builder()
            .storeName(store.getName())
            .profile(store.getProfile())
            .introduce(store.getIntroduce())
            .storeId(store.getId())
            .isWished(isWished)
            .build();
    }

    public SliceImpl<StoreAllBoardDto> getAllBoard(int page, Long memberId, Long storeId) {
        return isDefaultMember(memberId) ?
            storeRepository.getAllBoardWithLike(PageRequest.of(page, PAGE_SIZE), memberId, storeId) :
                storeRepository.getAllBoard(PageRequest.of(page, PAGE_SIZE), storeId);
    }

    public StoreCustomPage<List<StoreResponseDto>> getList(Long cursorId, Long memberId) {
        return storeRepository.getStoreList(cursorId, memberId);
    }

    private boolean getIsWished(Long memberId, Long storeId) {
        return (isDefaultMember(memberId)
            || wishListStoreRepository.findWishListStore(memberId, storeId).isPresent()
        );
    }

    private boolean isDefaultMember(Long memberId) {
        return memberId == 1L;
    }
}
