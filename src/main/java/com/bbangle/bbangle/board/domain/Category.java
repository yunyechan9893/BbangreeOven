package com.bbangle.bbangle.board.domain;

import java.util.Arrays;

public enum Category {
    BREAD,
    COOKIE,
    TART,
    JAM,
    CAKE,
    YOGURT,
    ETC;

    public static boolean checkCategory(String category) {
        return Arrays.stream(Category.values())
            .anyMatch(e -> e.name()
                .equals(category));
    }

}
