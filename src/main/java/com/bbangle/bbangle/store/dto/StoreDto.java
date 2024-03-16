package com.bbangle.bbangle.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record StoreDto(
    Long storeId,
    String storeName,
    String profile,
    Boolean isWished,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String introduce
) {
    // 아이디, 이름, 프로필, isWished의 값이 없다면 예외 발생
}
