package com.bbangle.bbangle.board.dto;

import org.springframework.web.bind.annotation.RequestParam;

public record FilterRequest(
    Boolean glutenFreeTag,
    Boolean highProteinTag,
    Boolean sugarFreeTag,
    Boolean veganTag,
    Boolean ketogenicTag,
    String category,
    Integer minPrice,
    Integer maxPrice,
    Boolean orderAvailableToday
) {

}
