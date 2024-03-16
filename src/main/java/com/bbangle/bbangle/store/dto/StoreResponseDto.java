package com.bbangle.bbangle.store.dto;

import com.bbangle.bbangle.store.domain.Store;
import lombok.Builder;

@Builder
public record StoreResponseDto(
    Long storeId,
    String storeName,
    String introduce,
    String profile,
    Boolean isWished
) {

    public static StoreResponseDto fromWithoutLogin(Store store) {
        return new StoreResponseDto(
            store.getId(),
            store.getName(),
            store.getIntroduce(),
            store.getProfile(),
            false);
    }

}
