package com.bbangle.bbangle.token.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OAut2User의 attribute를 서비스 유형에 맞게 담아줄 서비스
 */
@Getter
@RequiredArgsConstructor
public class OAuthAttributes {

    private Map<String, Object> attributes;     // OAuth2 반환하는 유저 정보
    private String nameAttributesKey;
    private String name;
    private String email;
    private String nickname;
    private String profile;
    private String provider;

    @Builder
    public OAuthAttributes(
        Map<String, Object> attributes, String nameAttributesKey, String name, String email,
        String gender, String profile, String nickname, String provider
    ) {
        this.attributes = attributes;
        this.nameAttributesKey = nameAttributesKey;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
        this.provider = provider;
    }


    public static OAuthAttributes of(String socialName, Map<String, Object> attributes) {
        if ("kakao".equals(socialName)) {
            return ofKakao("id", attributes);
        } else if ("google".equals(socialName)) {
            return ofGoogle("sub", attributes);
        }
        return null;
    }

    private static OAuthAttributes ofGoogle(
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        return OAuthAttributes.builder()
            .name(String.valueOf(attributes.get("name")))
            .email(String.valueOf(attributes.get("email")))
            .profile(String.valueOf(attributes.get("picture")))
            .attributes(attributes)
            .nameAttributesKey(userNameAttributeName)
            .provider("google")
            .build();
    }

    private static OAuthAttributes ofKakao(
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
            .nickname(String.valueOf(kakaoProfile.get("nickname")))
            .profile(String.valueOf(kakaoProfile.get("profile_image_url")))
            .nameAttributesKey(userNameAttributeName)
            .attributes(attributes)
            .provider("kakao")
            .build();
    }

}
