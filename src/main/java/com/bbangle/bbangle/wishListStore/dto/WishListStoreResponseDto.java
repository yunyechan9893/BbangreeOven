package com.bbangle.bbangle.wishListStore.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WishListStoreResponseDto {
    private String introduce;
    private String storeName;
    private Long storeId;

    @QueryProjection
    public WishListStoreResponseDto(String introduce, String storeName, Long storeId) {
        this.introduce = introduce;
        this.storeName = storeName;
        this.storeId = storeId;
    }
}
