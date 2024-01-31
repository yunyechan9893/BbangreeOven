package com.bbangle.bbangle.model;

import lombok.Getter;

@Getter
public enum SortType {
    RECENT("recent"), LOW_PRICE("low_price"), POPULAR("popular"), RECOMMEND("recommend");

    private final String value;
    private static final String ERROR_WORD = "올바르지 않은 분류 값입니다. 다시 선택해주세요";

    SortType(String value) {
        this.value = value;
    }

    public static SortType fromString(String value) {
        for (SortType type : SortType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException(ERROR_WORD);
    }
}
