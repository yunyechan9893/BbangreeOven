package com.bbangle.bbangle.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record StoreDetailResponseDto(
    StoreDto store,

    List<StoreBestBoardDto> bestProducts
) {

}
