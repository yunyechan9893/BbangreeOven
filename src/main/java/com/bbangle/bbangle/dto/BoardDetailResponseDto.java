package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BoardDetailResponseDto(
    StoreDto store,
    BoardDto board
) {

}


