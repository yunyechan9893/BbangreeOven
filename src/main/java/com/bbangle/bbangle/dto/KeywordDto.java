package com.bbangle.bbangle.dto;

import com.querydsl.core.annotations.QueryProjection;

public record KeywordDto(
    String keyword
) {

    @QueryProjection
    public KeywordDto(String keyword) {
        this.keyword = keyword;
    }

}
