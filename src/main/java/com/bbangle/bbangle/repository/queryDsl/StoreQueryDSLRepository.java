package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.StoreDetailResponseDto;

public interface StoreQueryDSLRepository {
    StoreDetailResponseDto getStoreDetailResponseDto(Long storeId);
}
