package com.bbangle.bbangle.exception;

public class MemberNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "해당 user가 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

}
