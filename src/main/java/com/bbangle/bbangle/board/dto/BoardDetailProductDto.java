package com.bbangle.bbangle.board.dto;

import lombok.Builder;

@Builder
public record BoardDetailProductDto(
    Long productId,
    String productTitle,
    Boolean glutenFreeTag,
    Boolean highProteinTag,
    Boolean sugarFreeTag,
    Boolean veganTag,
    Boolean ketogenicTag
) {

    public static BoardDetailProductDto from(ProductDto productDto) {
        return BoardDetailProductDto.builder()
            .productId(productDto.productId())
            .productTitle(productDto.productTitle())
            .glutenFreeTag(productDto.glutenFreeTag())
            .highProteinTag(productDto.highProteinTag())
            .sugarFreeTag(productDto.sugarFreeTag())
            .veganTag(productDto.veganTag())
            .ketogenicTag(productDto.ketogenicTag())
            .build();
    }

}
