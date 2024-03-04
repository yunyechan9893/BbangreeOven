package com.bbangle.bbangle.util;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class mNode {

    private String value;
    private Map<Character, mNode> children;
    private boolean isWordExists;

    public mNode(String value) {
        this.value = value;
        this.children = new HashMap<>();
        this.isWordExists = false;
    }

}
