package com.bbangle.bbangle.exception;

public class ExceedNicknameLengthException extends RuntimeException {

    private static final String MESSAGE = "닉네임은 20자 제한이에요!";

    public ExceedNicknameLengthException() {
        super(MESSAGE);
    }

}
