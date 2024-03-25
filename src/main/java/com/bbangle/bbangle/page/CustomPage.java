package com.bbangle.bbangle.page;

import lombok.Builder;
import net.minidev.json.annotate.JsonIgnore;

@Builder
public record CustomPage<T>(
    T content,
    int page,
    boolean isLast,
    @JsonIgnore
    int boardCnt,
    @JsonIgnore
    int storeCnt
) {

}
