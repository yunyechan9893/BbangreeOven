package com.bbangle.bbangle.configuration;

import com.bbangle.bbangle.token.jwt.TokenProvider;
import com.bbangle.bbangle.member.domain.Member;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenGenerator {

    @Autowired
    TokenProvider tokenProvider;

//    @Test
    @DisplayName("토큰_생성_테스트")

    void test() throws Exception {
        //given
        Member member = Member.builder().id(23L).build();
        String token = tokenProvider.generateToken(member, Duration.ofDays(1));

        //when
        System.out.println(token);
    }

}
