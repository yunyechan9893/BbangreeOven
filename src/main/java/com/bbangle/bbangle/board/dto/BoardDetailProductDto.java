package com.bbangle.bbangle.board.dto;

import com.bbangle.bbangle.board.domain.Category;
import lombok.Builder;

@Builder
public record BoardDetailProductDto(
    Long productId,
    String productTitle,
    Boolean gluten_free_tag,
    Boolean high_protein_tag,
    Boolean sugar_free_tag,
    Boolean vegan_tag,
    Boolean ketogenic_tag
) {


}
