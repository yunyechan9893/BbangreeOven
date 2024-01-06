package com.bbangle.bbangle.service;

import com.bbangle.bbangle.config.jwt.TokenProvider;
import com.bbangle.bbangle.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * The type Token service.
 */
@RequiredArgsConstructor
@Service
public class TokenService {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * 액세스 토큰 새로 생성
     *
     * @param memberId 멤버 id
     * @return the string 토큰
     */
    public String createNewAccessToken(Long memberId){
        Long id = refreshTokenService.findByMemberId(memberId).getMemberId();
        Member member = memberService.findById(id);
        return tokenProvider.generateToken(member, Duration.ofHours(1));
    }
}
