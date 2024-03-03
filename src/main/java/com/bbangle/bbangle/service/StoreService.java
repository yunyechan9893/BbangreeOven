package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.StoreAllBoardDto;
import com.bbangle.bbangle.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.dto.StoreResponseDto;
import com.bbangle.bbangle.service.impl.StoreServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public interface StoreService {
    StoreDetailResponseDto getStoreDetailResponse(Long memberId, Long StoreId);

    SliceImpl<StoreAllBoardDto> getAllBoard(int page,Long memberId, Long StoreId);

    Slice<StoreResponseDto> getList(Pageable pageable);

}
