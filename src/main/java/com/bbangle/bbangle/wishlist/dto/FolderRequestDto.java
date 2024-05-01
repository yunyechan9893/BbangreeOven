package com.bbangle.bbangle.wishlist.dto;

import com.bbangle.bbangle.wishlist.validator.WishListFolderValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FolderRequestDto(
    String title
) {

    public FolderRequestDto {
        WishListFolderValidator.validate(title);
    }

}
