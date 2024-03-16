package com.bbangle.bbangle.search.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record RecencySearchResponse(
    List<KeywordDto> content
) {

}
