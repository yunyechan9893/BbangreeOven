package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Builder
public record BoardDetailDto(
        Long boardId,
        String thumbnail,
        List<BoardImgDto> images,
        String title,
        int price,
        BoardAvailableDayDto orderAvailableDays,
        String purchaseUrl,
        Boolean isWished,
        Boolean isBundled,
        String detail,
        List<String> tags,
        List<ProductDto> products
) {}
