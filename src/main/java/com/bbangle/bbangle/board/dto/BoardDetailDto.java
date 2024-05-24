package com.bbangle.bbangle.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BoardDetailDto {

    private Integer order;
    private String imageUrl;

    @QueryProjection
    public BoardDetailDto(Integer order, String imageUrl) {
        this.order = order;
        this.imageUrl = imageUrl;
    }
}
