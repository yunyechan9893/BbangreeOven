package com.bbangle.bbangle.analytics.service;

import com.bbangle.bbangle.AbstractIntegrationTest;
import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.store.domain.Store;
import com.bbangle.bbangle.token.oauth.domain.OauthServerType;
import com.bbangle.bbangle.wishlist.domain.WishListBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsServiceTest extends AbstractIntegrationTest {


    @Test
    @DisplayName("신규 회원의 수가 정상적으로 조회된다.")
    void countNewMember() {
        // given
        createOldMembers();
        createNewMembers();

        // then
        long result = analyticsService.countNewMember();

        // then
        assertThat(result).isEqualTo(20);
    }

    @Test
    @DisplayName("전체 회원의 수가 정상적으로 조회된다.")
    void countAllMember() {
        // given
        createOldMembers();
        createNewMembers();

        // when
        long result = analyticsService.countAllMember();

        // then
        assertThat(result).isEqualTo(20);
    }

    @Test
    @DisplayName("회원 대비 위시리스트 이용 비율이 성공적으로 조회된다.")
    void countMembersUsingWishlist() {
        // given
        List<Member> oldMemberList1 = createOldMembers();
        List<Member> oldMemberList2 = createOldMembers();
        List<Member> oldMemberList3 = createOldMembers();
        createWishListBoards(oldMemberList1);

        // when
        String result = analyticsService.countMembersUsingWishlist();


        // then
        assertThat(result).isEqualTo("33.33");
    }


    @Test
    @DisplayName("게시글 별 위시리스트 순위가 정상적으로 조회된다.")
    void getWishlistBoardRanking() {
        // given
        List<Member> oldMemberList = createOldMembers();
        createBoards(oldMemberList);

        // when
        List<Board> result = analyticsService.getWishlistBoardRanking();
        List<Integer> expect = List.of(10, 9, 8, 7, 6, 5, 4, 3, 2, 1);


        // then
        List<Integer> wishCntRankingList = result.stream()
                .map(Board::getWishCnt)
                .toList();

        assertThat(wishCntRankingList).isEqualTo(expect);
    }


    // TODO: createAt을 지정할 수 있어야 7일 전 회원 데이터를 생성할 수 있음
    private List<Member> createOldMembers() {
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

        return memberRepository.saveAll(memberList);
    }


    private List<Member> createNewMembers() {
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

        return memberRepository.saveAll(memberList);
    }


    private void createWishListBoards(List<Member> memberList) {
        for(int i = 0; i < memberList.size(); i++){
            Store store = Store.builder()
                    .id((long) i + 1)
                    .name("test" + i)
                    .introduce("introduce" + i)
                    .isDeleted(false)
                    .build();
            storeRepository.save(store);

            Board board = Board.builder()
                    .id((long) i + 1)
                    .store(store)
                    .title("title" + i)
                    .build();
            boardRepository.save(board);

            WishListBoard wishListBoard = WishListBoard.builder()
                    .memberId(memberList.get(i).getId())
                    .boardId(board.getId())
                    .build();
            wishListBoardRepository.save(wishListBoard);
        }
    }


    private void createBoards(List<Member> members) {
        for(int i = 1; i <= members.size(); i++){
            Store store = Store.builder()
                    .id((long) i + 1)
                    .name("test" + i)
                    .introduce("introduce" + i)
                    .isDeleted(false)
                    .build();
            storeRepository.save(store);

            Board board = Board.builder()
                    .id((long) i + 1)
                    .store(store)
                    .title("title" + i)
                    .wishCnt(i)
                    .build();
            boardRepository.save(board);

        }
    }

}