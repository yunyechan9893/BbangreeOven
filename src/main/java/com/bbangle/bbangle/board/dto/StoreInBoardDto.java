package com.bbangle.bbangle.board.dto;

import com.bbangle.bbangle.store.domain.Store;

public record StoreInBoardDto(
    Long storeId,
    String storeTitle,
    String storeProfile,
    Boolean isWished
) {

    public static StoreInBoardDto of(Store store, boolean isWished) {
        return new StoreInBoardDto(
            store.getId(),
            store.getName(),
            store.getProfile(),
            isWished
        );
    }
}
