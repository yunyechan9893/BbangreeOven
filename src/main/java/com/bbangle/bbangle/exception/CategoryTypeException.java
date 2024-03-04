package com.bbangle.bbangle.exception;


public class CategoryTypeException extends RuntimeException {

    private static final String MESSAGE = "올바르지 않은 Category 입니다. 다시 입력해주세요";

    public CategoryTypeException() {
        super(MESSAGE);
    }

}
