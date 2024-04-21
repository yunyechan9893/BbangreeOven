package com.bbangle.bbangle.fixture;

import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.token.oauth.domain.OauthServerType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberFixture {

    private static final Faker faker = new Faker();

    public static Member createKakaoMember(){
        String randomProviderId = String.valueOf(faker.random()
            .nextLong(0, 10_000_000));

        return Member.builder()
            .provider(OauthServerType.KAKAO)
            .providerId(randomProviderId)
            .build();
    }

}
