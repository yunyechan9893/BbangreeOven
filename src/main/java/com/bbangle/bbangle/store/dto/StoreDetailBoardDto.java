package com.bbangle.bbangle.store.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import net.datafaker.providers.base.Bool;

@Builder
public record StoreDetailBoardDto(
    Long storeId,
    Long boardId,
    String boardProfile,
    String boardTitle,
    Integer boardPrice,
    Integer boardView,
    Boolean isWished
) {

}
