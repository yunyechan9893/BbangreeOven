package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BoardResponseDto(
        Long boardId,
        Long storeId,
        String storeName,
        String thumbnail,
        String title,
        int price,
        ProductTagDto tagDto
) {

}