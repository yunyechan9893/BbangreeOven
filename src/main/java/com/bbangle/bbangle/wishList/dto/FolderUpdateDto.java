package com.bbangle.bbangle.wishList.dto;

import com.bbangle.bbangle.wishList.validator.WishListFolderValidator;

public record FolderUpdateDto(
    String title
) {

    public FolderUpdateDto {
        WishListFolderValidator.validateTitle(title);
    }

}
