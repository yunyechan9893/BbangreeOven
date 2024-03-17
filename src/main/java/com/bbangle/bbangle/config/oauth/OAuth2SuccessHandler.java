package com.bbangle.bbangle.config.oauth;

import com.bbangle.bbangle.config.jwt.TokenProvider;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.service.MemberService;
import com.bbangle.bbangle.token.domain.RefreshToken;
import com.bbangle.bbangle.common.redis.repository.RefreshTokenRepository;
import com.bbangle.bbangle.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(3);

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationReqBasedOnCookieRepository authorizationRequestRepository;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String redirectPath = "";
        String requestURL = request.getRequestURL().toString();
        requestURL = requestURL.substring(0, requestURL.indexOf("/login"));
        if(requestURL.equals("http://dev.bbangle.store")){
            redirectPath = "http://localhost:3000/";
        }else if(requestURL.equals("https://api.bbangle.store")){
            redirectPath = "http://www.bbangle.store/";
        }


        Map<String, Object> attributes = oAuth2User.getAttributes();
        Member member = null;
        //google
        if (attributes.containsKey("sub")) {
            member = memberService.findByEmail((String) oAuth2User.getAttributes()
                .get("email"));
            //kakao
        } else if (attributes.containsKey("id")) {
            LinkedHashMap propertiesMap = (LinkedHashMap) oAuth2User.getAttributes()
                .get("properties");
            String nickname = (String) propertiesMap.get("nickname");
            member = memberService.findByNickname(nickname);
        }

        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(), refreshToken);
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
        addTokensToCookie(request, response, refreshToken, accessToken);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectPath)
            .build()
            .toUriString();

        //clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * 리프레시 토큰 DB에 저장
     *
     * @param memberId        the member id
     * @param newRefreshToken the new refresh token
     */
    public void saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
            .map(entity -> entity.update(newRefreshToken))
            .orElse(new RefreshToken(memberId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 액세스 토큰과 리프레시 토큰 쿠키에 담기.
     *
     * @param request      the request
     * @param response     the response
     * @param refreshToken the refresh token
     * @param accessToken  the access token
     */
    private void addTokensToCookie(
        HttpServletRequest request, HttpServletResponse response,
        String refreshToken, String accessToken
    ) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);

        cookieMaxAge = (int) ACCESS_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, ACCESS_TOKEN_COOKIE_NAME, accessToken, cookieMaxAge);
    }


    /**
     * 인증 관련 설정값 , 쿠키 삭제
     *
     * @param request  the request
     * @param response the response
     */
    private void clearAuthenticationAttributes(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
