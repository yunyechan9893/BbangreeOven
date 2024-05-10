package com.bbangle.bbangle.store.dto;

import com.bbangle.bbangle.board.domain.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record PopularBoardDto(
    Long rank,
    Long boardId,
    String boarProfile,
    String boardTitle,
    Integer boardPrice,
    Category category,
    Long wishlistBoardId
) {

    @QueryProjection
    public PopularBoardDto {
    }

    public PopularBoardResponse toPopularBoardResponse() {
        return PopularBoardResponse.builder()
            .boardId(boardId)
            .boardProfile(boarProfile)
            .boardTitle(boardTitle)
            .boardPrice(boardPrice)
            .isWished(isEmptyWishlist(wishlistBoardId))
            .isBundled(false)
            .build();
    }

    private Boolean isEmptyWishlist(Long wishlistId) {
        return wishlistId != null && wishlistId > 0;
    }
}
