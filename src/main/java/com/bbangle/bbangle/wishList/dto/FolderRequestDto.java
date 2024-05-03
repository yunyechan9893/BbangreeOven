package com.bbangle.bbangle.wishList.dto;

import com.bbangle.bbangle.wishList.validator.WishListFolderValidator;

public record FolderRequestDto(
    String title
) {

    public FolderRequestDto {
        WishListFolderValidator.validateTitle(title);
    }

}
