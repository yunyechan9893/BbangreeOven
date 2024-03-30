package com.bbangle.bbangle.store.service;

import com.bbangle.bbangle.board.dto.StoreAllBoardDto;
import com.bbangle.bbangle.store.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public interface StoreService {

    StoreDetailResponseDto getStoreDetailResponse(Long memberId, Long StoreId);

    SliceImpl<StoreAllBoardDto> getAllBoard(int page,Long memberId, Long StoreId);

    Slice<StoreResponseDto> getList(Pageable pageable);

}
