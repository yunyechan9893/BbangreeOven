package com.bbangle.bbangle.wishList.dto;

import jakarta.validation.constraints.NotNull;

public record WishProductRequestDto(
    @NotNull
    Long folderId
) {

}
