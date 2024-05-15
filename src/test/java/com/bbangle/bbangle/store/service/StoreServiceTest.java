package com.bbangle.bbangle.store.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.bbangle.bbangle.AbstractIntegrationTest;
import com.bbangle.bbangle.fixture.MemberFixture;
import com.bbangle.bbangle.fixture.StoreFixture;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.page.StoreCustomPage;
import com.bbangle.bbangle.store.domain.Store;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StoreServiceTest extends AbstractIntegrationTest {

    private static final Long NULL_CURSOR = null;
    private static final Long NULL_MEMBER_ID = null;

    private Member member;

    @BeforeEach
    void setup() {
        wishListStoreRepository.deleteAll();
        wishListFolderRepository.deleteAll();
        storeRepository.deleteAll();
        memberRepository.deleteAll();

        member = MemberFixture.createKakaoMember();
        member = memberService.getFirstJoinedMember(member);
    }

    @Nested
    @DisplayName("store 조회 서비스 로직 테스트")
    class GetStoreList {

        @BeforeEach
        public void saveStoreList() {
            for (int i = 0; i < 30; i++) {
                Store store = StoreFixture.storeGenerator();
                storeRepository.save(store);
            }
        }

        @Test
        @DisplayName("정상적으로 첫 페이지를 조회한다")
        void getFirstPage() {
            //given, when
            StoreCustomPage<List<StoreResponseDto>> list = storeService.getList(
                NULL_CURSOR,
                NULL_MEMBER_ID
            );
            List<StoreResponseDto> content = list.getContent();
            Boolean hasNext = list.getHasNext();

            //then
            assertThat(content).hasSize(20);
            assertThat(hasNext).isTrue();
        }

        @Test
        @DisplayName("정상적으로 마지막 페이지를 조회한다")
        void getLastPage() {
            //given
            StoreCustomPage<List<StoreResponseDto>> firstPage = storeService.getList(NULL_CURSOR,
                NULL_MEMBER_ID);
            Long nextCursor = firstPage.getNextCursor();

            //when
            StoreCustomPage<List<StoreResponseDto>> lastPage = storeService.getList(nextCursor,
                NULL_MEMBER_ID);

            List<StoreResponseDto> lastPageContent = lastPage.getContent();
            Boolean lastPageHasNext = lastPage.getHasNext();
            Long lastPageNextCursor = lastPage.getNextCursor();

            //then
            assertThat(lastPageContent).hasSize(10);
            assertThat(lastPageHasNext).isFalse();
        }

        @Test
        @DisplayName("마지막 자료를 조회하는 경우 nextCursor는 -1을 가리킨다")
        void getLastContent() {
            //given, when
            StoreCustomPage<List<StoreResponseDto>> firstPage = storeService.getList(NULL_CURSOR,
                NULL_MEMBER_ID);
            Long nextCursor = firstPage.getNextCursor();
            StoreCustomPage<List<StoreResponseDto>> lastPage = storeService.getList(nextCursor,
                NULL_MEMBER_ID);
            Long lastContentCursor = lastPage.getNextCursor();

            StoreCustomPage<List<StoreResponseDto>> noContent = storeService.getList(
                lastContentCursor,
                NULL_MEMBER_ID);

            //then
            assertThat(noContent.getContent()).hasSize(0);
            assertThat(noContent.getNextCursor()).isEqualTo(-1L);
        }

        @Test
        @DisplayName("좋아요를 누른 store는 isWished가 true로 반환된다")
        void getWishedContent() throws Exception {
            //given, when
            StoreCustomPage<List<StoreResponseDto>> before = storeService.getList(NULL_CURSOR,
                NULL_MEMBER_ID);
            StoreResponseDto first = before.getContent()
                .stream()
                .findFirst()
                .orElseThrow(Exception::new);
            wishListStoreService.save(member.getId(), first.getStoreId());

            //then
            StoreResponseDto wishedContent = storeService.getList(NULL_CURSOR, member.getId())
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(Exception::new);
            assertThat(wishedContent.getIsWished()).isTrue();
        }

    }

}
