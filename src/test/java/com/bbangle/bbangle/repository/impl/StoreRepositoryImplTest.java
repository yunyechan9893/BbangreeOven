package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.dto.StoreDetailResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StoreRepositoryImplTest {

    @Autowired
    StoreRepositoryImpl storeRepository;

    @Test
    public void getStoreDetailResponseDtoTest(){
        Long storeId = Long.valueOf(1);
        StoreDetailResponseDto result = storeRepository.getStoreDetailResponseDto(storeId);
        System.out.println(result);
    }
}
