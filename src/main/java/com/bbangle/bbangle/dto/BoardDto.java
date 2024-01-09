package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import java.util.List;

@Builder
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
        List<ProductDto> products
) {
}
