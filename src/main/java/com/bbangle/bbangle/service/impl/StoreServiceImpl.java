package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.repository.impl.StoreRepositoryImpl;
import com.bbangle.bbangle.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepositoryImpl storeRepository;

    @Override
    public StoreDetailResponseDto getStoreDetailResponse(Long StoreId) {
        return storeRepository.getStoreDetailResponseDto(StoreId);
    }
}
