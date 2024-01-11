package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BoardDto(
        Long id,
        String profile,
        List<String> images,
        String title,
        int price,
        BoardAvailableDayDto orderAvailableDays,
        String purchaseUrl,
        Boolean isWished,
        Boolean isBundled,
        String detail,

        @JsonProperty("tags")
        ProductTagDto allTags,
        List<ProductDto> products
) {
}
