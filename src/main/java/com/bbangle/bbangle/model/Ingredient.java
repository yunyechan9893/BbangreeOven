package com.bbangle.bbangle.model;

import lombok.Getter;

@Getter
public enum Ingredient {
    GLUTEN_FREE("glutenFree"),
    HIGH_PROTEIN("highProtein"),
    SUGAR_FREE("sugarFree"),
    VEGAN("vegan"),
    KETOGENIC("ketogenic");

    private final String name;

    Ingredient(String name) {
        this.name = name;
    }

}
