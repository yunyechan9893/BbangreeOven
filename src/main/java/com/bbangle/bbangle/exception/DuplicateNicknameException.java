package com.bbangle.bbangle.exception;

public class DuplicateNicknameException extends RuntimeException {

    private static final String MESSAGE = "중복된 닉네임이에요!";

    public DuplicateNicknameException() {
        super(MESSAGE);
    }

}
