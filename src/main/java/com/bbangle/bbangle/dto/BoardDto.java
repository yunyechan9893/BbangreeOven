package com.bbangle.bbangle.dto;

import lombok.Builder;
import java.util.List;

@Builder
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
    TagDto tags,
    List<ProductDto> products
) {

}
