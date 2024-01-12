package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record StoreDetailResponseDto(
        @JsonProperty(value = "store")
        StoreDto storeDto,

        List<BoardDto> bestProducts,

        List<BoardDto> allProducts
) {
}
