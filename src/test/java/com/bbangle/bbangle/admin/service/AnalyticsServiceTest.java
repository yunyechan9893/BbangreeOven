package com.bbangle.bbangle.admin.service;

import com.bbangle.bbangle.AbstractIntegrationTest;
import com.bbangle.bbangle.analytics.service.AnalyticsService;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.token.oauth.domain.OauthServerType;
import com.bbangle.bbangle.wishlist.repository.WishListBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

class AnalyticsServiceTest extends AbstractIntegrationTest {

    @Autowired
    AnalyticsService analyticsService;
    @Autowired MemberRepository memberRepository;
    @Autowired WishListBoardRepository wishListBoardRepository;;


    @BeforeEach
    void setUp() {
        createNewMember();
        createOldMember();
    }

    @Test
    @DisplayName("신규 회원의 수가 정상적으로 조회된다.")
    void countNewMember() {
        // given & then
        long result = analyticsService.countNewMember();

        // then
        Assertions.assertThat(result).isEqualTo(20);
    }

    @Test
    @DisplayName("전체 회원의 수가 정상적으로 조회된다.")
    void countAllMember() {
        // given & when
        long result = analyticsService.countAllMember();

        // then
        Assertions.assertThat(result).isEqualTo(20);
    }

    @Test
    @DisplayName("회원 대비 위시리스트 이용 비율이 성공적으로 조회된다.")
    void countMembersUsingWishlist() {
        // given


        // when


        // then
    }


    // TODO: createAt을 지정할 수 있어야 7일 전 회원 데이터를 생성할 수 있음
    void createOldMember() {
        List<Member> memberList = new ArrayList<>();

        for(int i = 1; i <= 10; i++){
            Member member = Member.builder()
                    .email("test" + i + "@email.com")
                    .name("testUser" + i)
                    .provider(OauthServerType.KAKAO)
                    .isDeleted(false)
                    .build();

            memberList.add(member);
        }

        memberRepository.saveAll(memberList);
    }

    void createNewMember() {
        List<Member> memberList = new ArrayList<>();

        for(int i = 1; i <= 10; i++){
            Member member = Member.builder()
                    .email("test" + i + "@email.com")
                    .name("testUser" + i)
                    .provider(OauthServerType.KAKAO)
                    .isDeleted(false)
                    .build();

            memberList.add(member);
        }

        memberRepository.saveAll(memberList);
    }

}