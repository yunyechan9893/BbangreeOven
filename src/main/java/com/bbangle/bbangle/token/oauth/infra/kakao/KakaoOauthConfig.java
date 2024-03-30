package com.bbangle.bbangle.token.oauth.infra.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2.kakao")
public record KakaoOauthConfig(
    String redirectUri,
    String clientId,
    String clientSecret,
    String[] scope
) {

}
