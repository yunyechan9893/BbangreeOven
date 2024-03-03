package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.StoreDetailResponseDto;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Pageable;
import java.util.HashMap;


public interface StoreQueryDSLRepository {
    StoreDetailResponseDto getStoreDetailResponseDto(Long storeId);

    SliceImpl getAllBoard(Pageable pageable, Long storeId);


    HashMap<Long, String> getAllStoreTitle();
}
