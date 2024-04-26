package com.bbangle.bbangle.token.oauth;

import com.bbangle.bbangle.common.redis.repository.RefreshTokenRepository;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.token.domain.RefreshToken;
import com.bbangle.bbangle.token.jwt.TokenProvider;
import com.bbangle.bbangle.token.oauth.domain.OauthServerType;
import com.bbangle.bbangle.token.oauth.domain.client.OauthMemberClientComposite;
import com.bbangle.bbangle.token.oauth.infra.kakao.dto.LoginTokenResponse;
import com.bbangle.bbangle.wishList.dto.FolderRequestDto;
import com.bbangle.bbangle.wishList.service.WishListFolderService;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OauthService {

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(3);
    private static final String DEFAULT_FOLDER_NAME = "기본 폴더";
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final MemberRepository memberRepository;
    private final WishListFolderService folderService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginTokenResponse login(OauthServerType oauthServerType, String authCode) {
        Member oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        String nickname = oauthMember.getNickname();
        //TODO 구글, 카카오 식별자 필요
        //카카오
        Member saved = memberRepository.findByProviderAndProviderId(oauthMember.getProvider(),
                oauthMember.getProviderId())
            .orElseGet(() -> {
                Member newMember = Member.builder()
                    .nickname(nickname)
                    .provider(oauthMember.getProvider())
                    .providerId(oauthMember.getProviderId())
                    .profile(oauthMember.getProfile())
                    .build();
                memberRepository.save(newMember);
                Long newMemberId = newMember.getId();
                //기본 위시리스트 폴더 추가
                folderService.create(newMemberId, new FolderRequestDto(DEFAULT_FOLDER_NAME));
                return newMember;
            });
        String refreshToken = tokenProvider.generateToken(saved, REFRESH_TOKEN_DURATION);
        String accessToken = tokenProvider.generateToken(saved, ACCESS_TOKEN_DURATION);
        Optional<RefreshToken> refreshTokenByMemberId =
            refreshTokenRepository.findByMemberId(saved.getId());
        if(refreshTokenByMemberId.isEmpty()){
            saveRefreshToken(refreshToken, saved);
        }else {
            refreshTokenByMemberId.get().update(refreshToken);
        }
        return new LoginTokenResponse(accessToken, refreshToken);
    }

    private void saveRefreshToken(String refreshToken, Member saved) {
        RefreshToken saveToken = RefreshToken.builder()
            .refreshToken(refreshToken)
            .memberId(saved.getId())
            .build();

        refreshTokenRepository.save(saveToken);
    }

}
