package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.dto.StoreResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StoreService {

    StoreDetailResponseDto getStoreDetailResponse(Long StoreId);

    Slice<StoreResponseDto> getList(Pageable pageable);

}
