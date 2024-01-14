package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BoardDetailResponseDto(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    StoreDto store,
    BoardDto board
) {

}


