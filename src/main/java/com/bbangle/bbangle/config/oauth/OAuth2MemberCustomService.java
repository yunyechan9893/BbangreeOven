package com.bbangle.bbangle.config.oauth;

import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2MemberCustomService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 요청을 바탕으로 멤버 정보를 담은 객체 반환
        // 리소스 서버에서 보내주는 사용자 정보를 불러옴
        OAuth2User member = super.loadUser(userRequest);
        Map<String , Object> originAttributes = member.getAttributes();
        //OAuth 서비스 id(google, kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, originAttributes);
        saveOrUpdate(oAuthAttributes);

        //OAuth2User를 반환하면 알아서 Spring에서 세션에 자동 저장
        return member;
    }


    /**
     * 이미 존재하는 회원이라면 이름과 프로필 이미지르 업데이트
     * 없는 회원이라면 새로 등록
     *
     * @param oAuthAttributes 각 OAuthProvider 별 유저 정보를 담고 있는 클래스
     * @return the member
     */
    public Member saveOrUpdate(OAuthAttributes oAuthAttributes) {
        String email = oAuthAttributes.getEmail();
        String name = oAuthAttributes.getName();
        String profileImageUrl = oAuthAttributes.getProfileImageUrl();
        Member member = memberRepository.findByEmail(email)
                .map(entity -> entity.update(name, profileImageUrl))
                .orElse(oAuthAttributes.toEntity());
        return memberRepository.save(member);
    }

}
