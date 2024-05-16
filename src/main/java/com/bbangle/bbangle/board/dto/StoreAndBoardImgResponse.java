package com.bbangle.bbangle.board.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record StoreAndBoardImgResponse(
    Long storeId,
    String storeTitle,
    String storeProfile,
    Boolean isWished,
    List<BoardImgDto> boardImgs
) {

}
