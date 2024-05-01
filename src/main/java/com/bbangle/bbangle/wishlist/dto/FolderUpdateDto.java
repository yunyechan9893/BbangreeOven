package com.bbangle.bbangle.wishlist.dto;

import com.bbangle.bbangle.wishlist.validator.WishListFolderValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FolderUpdateDto(
    String title
) {

    public FolderUpdateDto {
        WishListFolderValidator.validateTitle(title);
    }

}
