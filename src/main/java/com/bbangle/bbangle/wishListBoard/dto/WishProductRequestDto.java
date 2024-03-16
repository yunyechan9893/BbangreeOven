package com.bbangle.bbangle.wishListBoard.dto;

import jakarta.validation.constraints.NotNull;

public record WishProductRequestDto(
    @NotNull
    Long folderId
) {

}
