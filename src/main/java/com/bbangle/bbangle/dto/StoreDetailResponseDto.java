package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
public record StoreDetailResponseDto(

    StoreDto store,

    List<BoardDto> bestProducts,

    List<BoardDto> allProducts
) {

}
