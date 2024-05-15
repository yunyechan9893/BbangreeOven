package com.bbangle.bbangle.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import net.datafaker.providers.base.Bool;

@Builder
public record StoreAndBoardImgResponse(
    Long storeId,
    String storeTitle,
    String storeProfile,
    Boolean isWished,
    List<BoardImgDto> boardImgs
) {

}
