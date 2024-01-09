package com.bbangle.bbangle.dto;

import lombok.Builder;

@Builder
public record ProductTagDto
    (
        boolean glutenFreeTag,
        boolean highProteinTag,
        boolean sugarFreeTag,
        boolean veganTag,
        boolean ketogenicTag
    ) {

}
