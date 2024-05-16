package com.bbangle.bbangle.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.Objects;

public record StoreAndBoardImgDto(
    Long storeId,
    String storeTitle,
    String storeProfile,
    Long boardImgId,
    String boardImgUrl,
    Long wishListStoreId
) {

    @QueryProjection
    public StoreAndBoardImgDto {
    }

    public BoardImgDto toBoardImgDto() {
        return BoardImgDto.builder()
            .boardImgId(boardImgId)
            .url(boardImgUrl)
            .build();
    }

    public Boolean isNonEmptyWishlist() {
        return Objects.nonNull(wishListStoreId) && wishListStoreId > 0;
    }

}
