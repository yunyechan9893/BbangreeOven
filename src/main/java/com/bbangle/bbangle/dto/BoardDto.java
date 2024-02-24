package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.util.List;

@Builder
public record BoardDto(
    Long boardId,
    String thumbnail,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<BoardImgDto> images,
    String title,
    int price,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    BoardAvailableDayDto orderAvailableDays,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String purchaseUrl,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isWished,
    Boolean isBundled,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer view,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String detail,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<String> tags,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ProductDto> products
) {

}
