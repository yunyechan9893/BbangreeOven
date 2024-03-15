package com.bbangle.bbangle.board.dto;

import com.bbangle.bbangle.board.domain.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductDto(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long id,
    String title,
    Category category,
    List<String> tags
) {

}
