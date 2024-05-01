package com.bbangle.bbangle.wishlist.service;

import static org.assertj.core.api.Assertions.*;

import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.fixture.MemberFixture;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.member.service.MemberService;
import com.bbangle.bbangle.wishlist.dto.FolderRequestDto;
import com.bbangle.bbangle.wishlist.dto.FolderResponseDto;
import com.bbangle.bbangle.wishlist.repository.WishListFolderRepository;
import java.util.List;

import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WishListFolderServiceTest {

    private static final Faker faker = new Faker();

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    WishListFolderRepository wishListFolderRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    WishListFolderService wishListFolderService;

    Member member;

    @BeforeEach
    public void setup() {
        wishListFolderRepository.deleteAll();
        memberRepository.deleteAll();

        member = MemberFixture.createKakaoMember();
        member = memberService.getFirstJoinedMember(member);
    }

    @Nested
    @DisplayName("위시리스트 폴더 생성 서비스 로직 테스트")
    class CreatWishListFolder {

        @Test
        @DisplayName("처음 가입한 멤버는 기본 폴더 하나만을 가지고 있다")
        public void memberWithFirstJoinedWishlistFolder() throws Exception {
            //given, when
            List<FolderResponseDto> folderList = wishListFolderService.getList(member.getId());

            //then
            assertThat(folderList).hasSize(1);
            FolderResponseDto basicFolder = folderList.get(0);
            assertThat(basicFolder.title()).isEqualTo("기본 폴더");
        }

        @Test
        @DisplayName("정상적인 이름의 위시리스트 폴더 생성을 요청하면 새로운 폴더가 만들어진다")
        public void memberCreateNewFolder() throws Exception {
            //given, when
            String title = faker.book()
                .title();
            if (title.length() > 12) {
                title = title.substring(0, 12);
            }
            FolderRequestDto folderRequestDto = new FolderRequestDto(title);
            wishListFolderService.create(member.getId(), folderRequestDto);

            List<FolderResponseDto> folderList = wishListFolderService.getList(member.getId());

            //then
            assertThat(folderList).hasSize(2);
            FolderResponseDto basicFolder = folderList.get(1);
            assertThat(basicFolder.title()).isEqualTo(title);
        }


        @ParameterizedTest
        @DisplayName("비정상적인 제목의 폴더는 만들 수 없다")
        @ValueSource(strings = {" ", "aaaaaaaaaaaaaaaaaaaaa"})
        @NullAndEmptySource
        public void cannotCrateFolderWithInvalidTitle(String title) throws Exception {
            //given, when, then
            assertThatThrownBy(() -> new FolderRequestDto(title))
                .isInstanceOf(BbangleException.class)
                .hasMessage(BbangleErrorCode.INVALID_FOLDER_TITLE.getMessage());
        }

        @Test
        @DisplayName("이미 이 유저가 만든 폴더의 제목과 동일한 제목의 폴더를 만드는 경우 예외가 발생한다")
        public void memberCreateNewFolderWithFolderNameAlreadyExist() throws Exception {
            //given
            String title = faker.book()
                .title();
            if (title.length() > 12) {
                title = title.substring(0, 12);
            }
            FolderRequestDto folderRequestDto = new FolderRequestDto(title);
            wishListFolderService.create(member.getId(), folderRequestDto);

            //when, then
            assertThatThrownBy(() -> wishListFolderService.create(member.getId(), folderRequestDto))
                .isInstanceOf(BbangleException.class)
                .hasMessage(BbangleErrorCode.FOLDER_NAME_ALREADY_EXIST.getMessage());
        }

        @Test
        @DisplayName("다른 유저가 만든 폴더와 같은 이름의 폴더를 만들더라도 정상적으로 만들어진다")
        public void createFolderWithSameTitleWithOthers() throws Exception {
            //given
            String title = faker.book()
                .title();
            if (title.length() > 12) {
                title = title.substring(0, 12);
            }
            FolderRequestDto folderRequestDto = new FolderRequestDto(title);
            wishListFolderService.create(member.getId(), folderRequestDto);

            Member kakaoMember = MemberFixture.createKakaoMember();
            Member firstJoinedMember = memberService.getFirstJoinedMember(kakaoMember);

            //when, then
            Assertions.assertDoesNotThrow(
                () -> wishListFolderService.create(firstJoinedMember.getId(), folderRequestDto));
        }

        @Test
        @DisplayName("10개의 wishList 폴더를 가지고 있는 경우 더이상 폴더를 만들 수 없다.")
        public void cannotCreateFolderMoreThan10() throws Exception {
            //given
            String title = faker.book()
                .title();
            if (title.length() > 12) {
                title = title.substring(0, 11);
            }
            for (int i = 0; i < 9; i++) {
                FolderRequestDto folderRequestDto = new FolderRequestDto(title + i);
                wishListFolderService.create(member.getId(), folderRequestDto);
            }

            FolderRequestDto folderRequestDto = new FolderRequestDto(title);

            //when, then
            assertThatThrownBy(() -> wishListFolderService.create(member.getId(), folderRequestDto))
                .isInstanceOf(BbangleException.class)
                .hasMessage(BbangleErrorCode.OVER_MAX_FOLDER.getMessage());

        }

    }

}
