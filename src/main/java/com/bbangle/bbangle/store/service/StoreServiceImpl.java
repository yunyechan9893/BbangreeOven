package com.bbangle.bbangle.store.service;


import java.util.List;
import java.util.stream.Collectors;

import com.bbangle.bbangle.board.dto.StoreAllBoardDto;

import com.bbangle.bbangle.store.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import com.bbangle.bbangle.store.domain.Store;
import com.bbangle.bbangle.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public StoreDetailResponseDto getStoreDetailResponse(Long memberId, Long storeId) {
        return memberId > 1L ?
                storeRepository.getStoreDetailResponseDtoWithLike(memberId, storeId) :
                storeRepository.getStoreDetailResponseDto(storeId);

    }

    @Override
    public SliceImpl<StoreAllBoardDto> getAllBoard(int page, Long memberId, Long storeId) {
        int PAGE_SIZE = 10;

        return memberId > 1L ?
                storeRepository.getAllBoardWithLike(PageRequest.of(page, PAGE_SIZE), memberId, storeId) :
                storeRepository.getAllBoard(PageRequest.of(page, PAGE_SIZE),storeId);
    }

    @Override
    public Slice<StoreResponseDto> getList(Pageable pageable) {
        Slice<Store> sliceBy = storeRepository.findSliceBy(pageable);

        List<StoreResponseDto> dtoList = sliceBy.getContent()
            .stream()
            .map(StoreResponseDto::fromWithoutLogin)
            .collect(Collectors.toList());

        return new SliceImpl<>(dtoList, pageable, sliceBy.hasNext());
    }


}
