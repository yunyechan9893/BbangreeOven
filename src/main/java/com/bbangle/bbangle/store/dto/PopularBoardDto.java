package com.bbangle.bbangle.store.dto;

import com.bbangle.bbangle.board.domain.Category;
import com.querydsl.core.annotations.QueryProjection;
import java.util.Objects;
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
            .isWished(isNonEmptyWishlist(wishlistBoardId))
            .isBundled(false)
            .build();
    }

    private Boolean isNonEmptyWishlist(Long wishlistId) {
        return Objects.nonNull(wishlistId) && wishlistId > 0;
    }
}