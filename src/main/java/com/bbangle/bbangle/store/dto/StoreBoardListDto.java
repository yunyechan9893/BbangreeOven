package com.bbangle.bbangle.store.dto;

import com.bbangle.bbangle.board.domain.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record StoreBoardListDto(
    Long boardId,
    String boardProfile,
    String boardTitle,
    Integer boardPrice,
    Integer boardView,
    Long productId,
    Boolean glutenFreeTag,
    Boolean highProteinTag,
    Boolean sugarFreeTag,
    Boolean veganTag,
    Boolean ketogenicTag,
    Category category,
    Long wishlistBoardId
) {

    @QueryProjection
    public StoreBoardListDto {
    }

    public StoreDetailBoardDto toBoardDto() {
        return StoreDetailBoardDto.builder()
            .boardId(this.boardId)
            .boardTitle(this.boardTitle)
            .boardProfile(this.boardProfile)
            .boardView(this.boardView)
            .boardPrice(this.boardPrice)
            .isWished(isEmptyWishlist(this.wishlistBoardId))
            .build();
    }

    public StoreDetailProductDto toProductDto() {
        return StoreDetailProductDto.builder()
            .boardId(this.boardId)
            .productId(this.productId)
            .ketogenicTag(this.ketogenicTag)
            .sugarFreeTag(this.sugarFreeTag)
            .highProteinTag(this.highProteinTag)
            .veganTag(this.veganTag)
            .glutenFreeTag(this.glutenFreeTag)
            .category(this.category)
            .build();
    }

    private Boolean isEmptyWishlist(Long wishlistId) {
        return wishlistId != null && wishlistId > 0;
    }
}
