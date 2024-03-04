package com.bbangle.bbangle.dto;

import lombok.Builder;

@Builder
public record NicknameCheckResponse(
    boolean isUsable
) {

}
