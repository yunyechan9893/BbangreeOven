package com.bbangle.bbangle.dto;

import lombok.Builder;

@Builder
public record BoardDetailResponseDto(
    StoreDto store,
    BoardDto board
) {

}


