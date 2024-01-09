package com.bbangle.bbangle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ProductTagDto(
        @JsonProperty(value = "gluten_free")
        boolean glutenFreeTag,
        @JsonProperty(value = "high_protein")
        boolean highProteinTag,
        @JsonProperty(value = "sugar_free")
        boolean sugarFreeTag,
        @JsonProperty(value = "vegan")
        boolean veganTag,
        @JsonProperty(value = "ketogenic")
        boolean ketogenicTag
) {
}
