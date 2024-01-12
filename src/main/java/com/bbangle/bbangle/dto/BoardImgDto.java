package com.bbangle.bbangle.dto;

import lombok.Builder;

@Builder
public record BoardImgDto(
        Long id,
        String url
) {
}
