package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record StoreDto(
        Long id,

        @JsonProperty(value = "title")
        String name,
        String profile,
        @JsonProperty(value = "is_wished")
        Boolean isWished) {
}
