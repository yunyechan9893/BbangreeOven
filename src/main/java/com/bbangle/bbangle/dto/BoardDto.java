package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BoardDto(
        Long id,
        String thumbnail,
        List<String> images,
        String title,
        int price,
        List<BoardAvailableDayDto> orderAvailableDays,
        String purchaseUrl,
        Boolean isWished,
        Boolean isBundled,
        String detail,
        List<ProductDto> products
) {
}
