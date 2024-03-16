package com.bbangle.bbangle.board.domain;

public enum TagEnum {

    GLUTEN_FREE("glutenFree"),
    HIGH_PROTEIN("highProtein"),
    SUGAR_FREE("sugarFree"),
    VEGAN("vegan"),
    KETOGENIC("ketogenic");

    private final String label;

    TagEnum(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
