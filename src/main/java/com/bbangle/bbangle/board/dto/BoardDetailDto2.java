package com.bbangle.bbangle.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record BoardDetailDto2(
        Long boardDetailId,
        Integer boardDetailOrder,
        String boardDetailurl
) {

    @QueryProjection
    public BoardDetailDto2 {

    }
}
