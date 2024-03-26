package com.bbangle.bbangle.config.oauth;

import com.bbangle.bbangle.BbangleApplication;
import com.bbangle.bbangle.BbangleApplication.WishListFolderService;
import com.bbangle.bbangle.config.jwt.TokenProvider;
import com.bbangle.bbangle.config.oauth.domain.OauthServerType;
import com.bbangle.bbangle.config.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.bbangle.bbangle.config.oauth.domain.client.OauthMemberClientComposite;
import com.bbangle.bbangle.config.oauth.infra.kakao.dto.LoginTokenResponse;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.wishListFolder.dto.FolderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final MemberRepository memberRepository;
    private final WishListFolderService folderService;
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(3);
    private static final String DEFAULT_FOLDER_NAME = "기본 폴더";
    private final TokenProvider tokenProvider;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType){
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public LoginTokenResponse login(OauthServerType oauthServerType, String authCode){
        Member oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        String nickname = oauthMember.getNickname();
        String profile = oauthMember.getProfile();
        //TODO 구글, 카카오 식별자 필요
        //카카오
        Member saved = memberRepository.findByNickname(nickname)
                .map(entity -> entity.updateNickname(nickname))
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .nickname(nickname)
                            .profile(profile)
                            .build();
                    memberRepository.save(newMember);
                    Long newMemberId = newMember.getId();
                    //기본 위시리스트 폴더 추가
                    folderService.create(newMemberId, new FolderRequestDto(DEFAULT_FOLDER_NAME));
                    return newMember;
                });
        String refreshToken = tokenProvider.generateToken(saved, REFRESH_TOKEN_DURATION);
        String accessToken = tokenProvider.generateToken(saved, ACCESS_TOKEN_DURATION);
        return new LoginTokenResponse(accessToken, refreshToken);
    }
}
