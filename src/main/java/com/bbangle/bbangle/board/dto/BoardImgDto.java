package com.bbangle.bbangle.board.dto;

import lombok.Builder;

@Builder
public record BoardImgDto(
    Long id,
    String url
) {

}
