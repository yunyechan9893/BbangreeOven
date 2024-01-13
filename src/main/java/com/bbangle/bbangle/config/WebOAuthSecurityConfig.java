package com.bbangle.bbangle.config;

import com.bbangle.bbangle.config.jwt.TokenAuthenticationFilter;
import com.bbangle.bbangle.config.jwt.TokenProvider;
import com.bbangle.bbangle.config.oauth.OAuth2AuthorizationReqBasedOnCookieRepository;
import com.bbangle.bbangle.config.oauth.OAuth2MemberCustomService;
import com.bbangle.bbangle.config.oauth.OAuth2SuccessHandler;
import com.bbangle.bbangle.repository.RefreshTokenRepository;
import com.bbangle.bbangle.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Slf4j
public class WebOAuthSecurityConfig {
    private final OAuth2MemberCustomService oAuth2MemberCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.trace("Configuring http filterChain");
        //헤더를 확인할 커스텀 필터 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        //토큰 재발급 URL은 인증 없이 접근 가능하도록 설정, 나머지 API는 인증 필요
        http.authorizeRequests()
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/**").authenticated()
                //Test시 위 2줄을 주석처리하시고 밑에 주석을 풀어주세요
                //.requestMatchers("/**").permitAll() //모든 경로에 인증 없이 접근
                .anyRequest().permitAll();

        http.oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                                //Authorization 요청과 관련된 상태 저장
                                .authorizationRequestRepository(oAuth2AuthorizationReqBasedOnCookieRepository()))
                        .successHandler(oAuth2SuccessHandler()) //인증 성공 시 실행할 핸들러
                        .userInfoEndpoint(infoEndpoint ->
                                infoEndpoint.userService(oAuth2MemberCustomService)));

        http.logout(logout -> logout.logoutSuccessUrl("/login"));

        http.exceptionHandling(except -> except.defaultAuthenticationEntryPointFor(
                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**")));

        return http.build();
    }
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationReqBasedOnCookieRepository(), memberService);
    }


    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationReqBasedOnCookieRepository oAuth2AuthorizationReqBasedOnCookieRepository(){
        return new OAuth2AuthorizationReqBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
