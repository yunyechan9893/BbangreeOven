package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ProductDto(

        @JsonProperty(value = "title")
        String name,
        @JsonProperty(value = "tags")
        ProductTagDto tagDto
) { }
