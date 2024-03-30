package com.bbangle.bbangle.admin.dto;


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
