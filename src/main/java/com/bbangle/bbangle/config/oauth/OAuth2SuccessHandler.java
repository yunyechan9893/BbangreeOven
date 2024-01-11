package com.bbangle.bbangle.config.oauth;

import com.bbangle.bbangle.config.jwt.TokenProvider;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.model.RefreshToken;
import com.bbangle.bbangle.repository.RefreshTokenRepository;
import com.bbangle.bbangle.service.MemberService;
import com.bbangle.bbangle.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REFRESH_TOKEN_DELIMITER_ID = "refresh_token_id";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String GOOGLE_REDIRECT_PATH = "/login/oauth2/code/google";
    public static final String KAKAO_REDIRECT_PATH = "/oauth2/kakao/callback";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationReqBasedOnCookieRepository oAuth2AuthorizationReqBasedOnCookieRepository;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Member member = memberService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        //리프레시 토큰 생성 -> 저장 -> 쿠키에 리프레시 토큰 ID 저장
        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        Long refreshTokenId = saveRefreshToken(member.getId(), refreshToken);
        addRefreshTokenIdToCookie(request, response, refreshTokenId);

        //액세스 토큰 생성 -> 헤더에 액세스 토큰 추가
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
        response.addHeader("token", accessToken);
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, GOOGLE_REDIRECT_PATH);
        //getRedirectStrategy().sendRedirect(request, response, KAKAO_REDIRECT_PATH);

    }


    /**
     * 인증 관련 설정값 , 쿠키 삭제
     *
     * @param request  the request
     * @param response the response
     */
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oAuth2AuthorizationReqBasedOnCookieRepository.removeAuthorizationRequestCookies(request, response);
    }


    /**
     * 리프레시 토큰의 id를 쿠키에 반환
     *
     * @param request        the request
     * @param response       the response
     * @param refreshTokenId the refresh token id
     */
    public void addRefreshTokenIdToCookie(HttpServletRequest request, HttpServletResponse response, Long refreshTokenId) {
        int cookieMaxAge = (int)REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_DELIMITER_ID);
        CookieUtil.addCookie(response, REFRESH_TOKEN_DELIMITER_ID, String.valueOf(refreshTokenId), cookieMaxAge);
    }


    /**
     * 생성된 리프레시 토큰을 저장하고 리프레시토큰 Id를 반환
     *
     * @param memberId        멤버 id
     * @param newRefreshToken 생성된 리프레시 토큰
     * @return 리프레시 토큰 Id
     */
    public Long saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(refreshTokenEntity -> refreshTokenEntity.update(newRefreshToken))
                .orElse(new RefreshToken(memberId, newRefreshToken));
        return refreshTokenRepository.save(refreshToken).getId();
    }
}
