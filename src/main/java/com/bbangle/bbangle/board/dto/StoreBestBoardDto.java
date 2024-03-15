package com.bbangle.bbangle.board.dto;

import lombok.Builder;

@Builder
public record StoreBestBoardDto(
        Long boardId,
        String thumbnail,
        String title,
        Integer price,
        Boolean isBundled
) {


}
//board.profile,
//        board.title,
//        board.price,
//        board.view
