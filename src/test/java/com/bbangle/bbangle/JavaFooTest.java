package com.bbangle.bbangle;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class JavaFooTest {
    private JavaFoo javaFoo = new JavaFoo();

    @Test
    public void partiallyCoveredHelloMethodTest() {
        String actual = javaFoo.hello("빵그리");
        assertEquals(actual, "파이팅");
    }
}
