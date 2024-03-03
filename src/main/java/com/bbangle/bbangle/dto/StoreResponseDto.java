package com.bbangle.bbangle.dto;

import com.bbangle.bbangle.model.Store;
import lombok.Builder;

@Builder
public record StoreResponseDto(
        Long storeId,
        String storeName,
        String introduce,
        String profile,
        Boolean isWished
    ){

    public static StoreResponseDto fromWithoutLogin(Store store){
        return new StoreResponseDto(
            store.getId(),
            store.getName(),
            store.getIntroduce(),
            store.getProfile(),
            false);
    }

}
