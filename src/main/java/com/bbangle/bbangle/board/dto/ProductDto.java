package com.bbangle.bbangle.board.dto;

import com.bbangle.bbangle.board.domain.Category;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductDto(
    Long productId,
    String productTitle,
    Category category,
    Boolean gluten_free_tag,
    Boolean high_protein_tag,
    Boolean sugar_free_tag,
    Boolean vegan_tag,
    Boolean ketogenic_tag

) {
    @QueryProjection
    public ProductDto {

    }

    public TagDto toTagDto(){
        return TagDto.builder()
            .gluten_free_tag(gluten_free_tag)
            .high_protein_tag(high_protein_tag)
            .sugar_free_tag(sugar_free_tag)
            .vegan_tag(vegan_tag)
            .ketogenic_tag(ketogenic_tag)
            .build();
    }
}
