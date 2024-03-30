package com.bbangle.bbangle.board.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record StoreAllBoardDto(
        Long boardId,
        String thumbnail,
        String title,
        Integer price,
        Boolean isWished,
        Boolean isBundled,
        List<String> tags,
        Integer view
) {
}
