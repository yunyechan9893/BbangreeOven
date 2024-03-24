package com.bbangle.bbangle.page;

public record CustomPage<T>(
    T content,
    int page,
    boolean isLast
) {

}
