package com.bbangle.bbangle.config.oauth.infra.kakao.client;

import com.bbangle.bbangle.config.oauth.infra.kakao.KakaoOauthConfig;
import com.bbangle.bbangle.config.oauth.infra.kakao.dto.KakaoMemberResponse;
import com.bbangle.bbangle.config.oauth.infra.kakao.dto.KakaoToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@RequiredArgsConstructor
@Component
public class KakaoApiClient {
    private final KakaoOauthConfig kakaoOauthConfig;

    private static final String AUTHCODE_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoToken fetchToken(String authCode){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", APPLICATION_FORM_URLENCODED_VALUE);
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("client_id", kakaoOauthConfig.clientId());
        body.put("redirect_uri", kakaoOauthConfig.redirectUri());
        body.put("code", authCode);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
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
