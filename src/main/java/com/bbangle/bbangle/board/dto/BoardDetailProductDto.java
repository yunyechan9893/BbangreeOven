package com.bbangle.bbangle.board.dto;

import com.bbangle.bbangle.board.domain.Category;
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


}
