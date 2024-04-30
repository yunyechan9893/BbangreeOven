package com.bbangle.bbangle.wishlist.dto;

import jakarta.validation.constraints.NotNull;

public record WishProductRequestDto(
    @NotNull
    Long folderId
) {

}
