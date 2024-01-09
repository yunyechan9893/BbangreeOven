package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BoardDetailResponseDto(
        @JsonProperty(value = "store")
        StoreDto storeDto,
        @JsonProperty(value = "board")
        BoardDto boardDto
) {
}


