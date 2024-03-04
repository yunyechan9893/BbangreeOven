package com.bbangle.bbangle.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record StoreDetailResponseDto(
    StoreDto store,

    List<StoreBestBoardDto> bestProducts
) {

}
