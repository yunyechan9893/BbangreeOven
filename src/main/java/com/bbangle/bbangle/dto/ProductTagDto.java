package com.bbangle.bbangle.dto;

import com.bbangle.bbangle.model.Product;
import lombok.Builder;

@Builder
public record ProductTagDto(
    boolean glutenFreeTag,
    boolean highProteinTag,
    boolean sugarFreeTag,
    boolean veganTag,
    boolean ketogenicTag
) {

    public static ProductTagDto from(Product product){
        return new ProductTagDto(
            product.isGlutenFreeTag(),
            product.isHighProteinTag(),
            product.isSugarFreeTag(),
            product.isVeganTag(),
            product.isKetogenicTag());
    }

}
