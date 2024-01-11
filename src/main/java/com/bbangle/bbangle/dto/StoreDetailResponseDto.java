package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record StoreDetailResponseDto(
        @JsonProperty(value = "store")
        StoreDto storeDto,

        @JsonProperty("best_products")
        List<BoardDto> bestProducts,

        @JsonProperty("all_products")
        List<BoardDto> allProducts
) {
}
