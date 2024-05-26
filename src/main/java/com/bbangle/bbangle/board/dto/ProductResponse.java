package com.bbangle.bbangle.board.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ProductResponse(
    Boolean boardIsBundled,
    List<BoardDetailProductDto> products
) {

    public static ProductResponse of(
        Boolean boardIsBundled,
        List<BoardDetailProductDto> products
    ) {
        return ProductResponse.builder()
            .boardIsBundled(boardIsBundled)
            .products(products)
            .build();
    }
}
