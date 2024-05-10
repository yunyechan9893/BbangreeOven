package com.bbangle.bbangle.store.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record StoreBoardsResponse(
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
