package com.bbangle.bbangle.store.dto;

import com.bbangle.bbangle.board.domain.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record StoreDetailStoreDto(
    Long storeId,
    String storeProfile,
    String storeTitle,
    String storeIntroduce,
    Long storeWishlistId
) {

    @QueryProjection
    public StoreDetailStoreDto {
    }

    public StoreResponse toStoreResponse() {
        return StoreResponse.builder()
            .storeId(storeId)
            .storeProfile(storeProfile)
            .storeTitle(storeTitle)
            .storeIntroduce(storeIntroduce)
            .isWished(isEmptyWishlist(storeWishlistId))
            .build();
    }

    private Boolean isEmptyWishlist(Long wishlistId) {
        return wishlistId > 0;
    }
}
