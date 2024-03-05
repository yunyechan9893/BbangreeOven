package com.bbangle.bbangle.dto;


import com.bbangle.bbangle.model.Category;

public record AdminProductRequestDto(
        String title,
        Integer price,
        String category,
        Boolean glutenFree,
        Boolean sugarFree,
        Boolean highProtein,
        Boolean vegan,
        Boolean ketogenic
) {
}
