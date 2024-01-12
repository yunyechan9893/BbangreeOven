package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ProductDto(
    Long boardId,
    String name,
    TagDto tags
) {

}
