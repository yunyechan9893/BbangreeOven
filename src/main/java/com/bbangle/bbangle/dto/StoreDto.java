package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record StoreDto(
    Long id,
    String name,
    String profile,

    Boolean isWished,

    String introduce
) {


}
