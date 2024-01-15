package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.util.List;

@Builder
public record ProductDto(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id,
    String title,
    List<String> tags
) {

}
