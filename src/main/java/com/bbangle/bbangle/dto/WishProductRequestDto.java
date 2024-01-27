package com.bbangle.bbangle.dto;

import jakarta.validation.constraints.NotNull;

public record WishProductRequestDto(
    @NotNull
    Long folderId
) {

}
