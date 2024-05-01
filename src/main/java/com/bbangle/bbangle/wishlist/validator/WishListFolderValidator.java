package com.bbangle.bbangle.wishlist.validator;

import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.exception.BbangleErrorCode;
import java.util.Objects;

public class WishListFolderValidator {

    public static void validate(String title) {
        if (Objects.isNull(title) || title.length() > 12) {
            throw new BbangleException(BbangleErrorCode.INVALID_FOLDER_TITLE);
        }
    }

}
