package com.bbangle.bbangle.config.oauth.infra.kakao.dto;

public record LoginTokenResponse(
        String accessToken,
        String refreshToken
) {


}
