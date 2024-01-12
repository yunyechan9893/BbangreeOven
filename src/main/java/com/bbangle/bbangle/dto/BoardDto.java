package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BoardDto(
        Long id,
        String profile,
        List<BoardImgDto> images,
        String title,
        int price,
        BoardAvailableDayDto orderAvailableDays,
        String purchaseUrl,
        Boolean isWished,
        Boolean isBundled,
        String detail,

        @JsonProperty("tags")
        TagDto allTags,
        List<ProductDto> products
) {
}
