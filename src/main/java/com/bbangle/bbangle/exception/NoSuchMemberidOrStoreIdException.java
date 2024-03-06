package com.bbangle.bbangle.exception;

public class NoSuchMemberidOrStoreIdException extends RuntimeException {

    private static final String MESSAGE = "스토어 찜을 해제하지 못했습니다";

    public NoSuchMemberidOrStoreIdException() {
        super(MESSAGE);
    }

}
