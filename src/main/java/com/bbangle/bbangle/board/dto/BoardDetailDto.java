package com.bbangle.bbangle.board.dto;

import com.querydsl.core.annotations.QueryProjection;

public record BoardDetailDto(
        Long boardId,
        Integer imgIndex,
        String url
) {
    @QueryProjection
    public BoardDetailDto(Long boardId, Integer imgIndex, String url){
        this.boardId = boardId;
        this.imgIndex = imgIndex;
        this.url = url;
    }
}
