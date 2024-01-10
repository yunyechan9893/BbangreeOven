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
        saveOrUpdate(member);
        return member;
    }

    /**
     * 유저가 있으면 업데이트, 없으면 유저 생성.
     *
     * @param oAuth2User 유저
     * @return 유저
     */
    private Member saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        Member member = memberRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(Member.builder()
                        .email(email)
                        .nickname(name)
                        .build());
        return memberRepository.save(member);
    }


}
