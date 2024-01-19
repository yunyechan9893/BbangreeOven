package com.bbangle.bbangle.model;

import java.util.Arrays;
import org.apache.coyote.BadRequestException;

public enum Category {
    BREAD, COOKIE, TART, JAM, CAKE, YOGURT, ETC;

    public static boolean checkCategory(String category) {
        return Arrays.stream(Category.values())
            .anyMatch(e -> e.name().equals(category));
    }

}
