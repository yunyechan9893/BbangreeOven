package com.bbangle.bbangle.dto;

import lombok.Builder;

@Builder
public record StoreDto(
    Long id,
    String name,
    String profile,
    Boolean isWished,
    String introduce
) {


}
