package com.bbangle.bbangle.token.oauth.infra.kakao.client;

import com.bbangle.bbangle.token.oauth.domain.OauthServerType;
import com.bbangle.bbangle.token.oauth.domain.client.OAuthMemberClient;
import com.bbangle.bbangle.token.oauth.infra.kakao.KakaoOauthConfig;
import com.bbangle.bbangle.token.oauth.infra.kakao.dto.KakaoMemberResponse;
import com.bbangle.bbangle.token.oauth.infra.kakao.dto.KakaoToken;
import com.bbangle.bbangle.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OAuthMemberClient {
    private final KakaoApiClient kakaoApiClient;
    private final KakaoOauthConfig kakaoOauthConfig;
    @Override
    public Member fetch(String authcode) {
        KakaoToken kakaoToken = kakaoApiClient.fetchToken(authcode);
        KakaoMemberResponse kakaoMemberResponse = kakaoApiClient.fetchMember("Bearer " + kakaoToken.accessToken());
        return kakaoMemberResponse.toMember();
    }

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }
}
