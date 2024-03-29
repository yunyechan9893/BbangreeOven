package com.bbangle.bbangle.token.oauth.infra.kakao.client;

import com.bbangle.bbangle.token.oauth.infra.kakao.KakaoOauthConfig;
import com.bbangle.bbangle.token.oauth.infra.kakao.dto.KakaoMemberResponse;
import com.bbangle.bbangle.token.oauth.infra.kakao.dto.KakaoToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@RequiredArgsConstructor
@Component
public class KakaoApiClient {
    private static final String AUTHCODE_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final RestTemplate restTemplate = new RestTemplate();

    private final KakaoOauthConfig kakaoOauthConfig;


    public KakaoToken fetchToken(String authCode){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoOauthConfig.clientId());
        body.add("redirect_uri", kakaoOauthConfig.redirectUri());
        body.add("code", authCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(AUTHCODE_URL, request, KakaoToken.class);
    }

    public KakaoMemberResponse fetchMember(String bearerToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);
        HttpEntity<String> request= new HttpEntity<>("", headers);
        ResponseEntity<KakaoMemberResponse> response = restTemplate.exchange(USERINFO_URL, HttpMethod.GET, request, KakaoMemberResponse.class);
        return response.getBody();
    }

}
