package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.StoreDetailResponseDto;
import java.util.HashMap;

public interface StoreQueryDSLRepository {

    StoreDetailResponseDto getStoreDetailResponseDto(Long storeId);

    HashMap<Long, String> getAllStoreTitle();

}
