package com.bbangle.bbangle.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record BoardDetailDto(
    Long boardDetailId,
    Integer boardDetailOrder,
    String boardDetailUrl
) {

    @QueryProjection
    public BoardDetailDto {

    }
}
