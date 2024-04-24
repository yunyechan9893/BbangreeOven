package com.bbangle.bbangle.board.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BoardDetailSelectDto(
        Long boardId,
        String thumbnail,
        List<BoardImgDto> images,
        String title,
        int price,
        BoardAvailableDayDto orderAvailableDays,
        String purchaseUrl,
        Boolean isWished,
        Boolean isBundled,
        List<BoardDetailDto> detail,
        List<String> tags,
        List<ProductDto> products
) {}
