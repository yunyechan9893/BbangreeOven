package com.bbangle.bbangle.board.dto;

import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.validator.BoardValidator;
import lombok.Builder;

@Builder
public record FilterRequest(
    Boolean glutenFreeTag,
    Boolean highProteinTag,
    Boolean sugarFreeTag,
    Boolean veganTag,
    Boolean ketogenicTag,
    Category category,
    Integer minPrice,
    Integer maxPrice,
    Boolean orderAvailableToday
) {

    public FilterRequest {
        BoardValidator.validateMinPrice(minPrice);
        BoardValidator.validateMaxPrice(maxPrice);
    }

}
