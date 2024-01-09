package com.bbangle.bbangle.dto;

import lombok.Builder;

@Builder
public record StoreDto(
        int id,
        String name,
        String thumbnail,
        Boolean is_wished) {
}
