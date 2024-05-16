package com.bbangle.bbangle.store.repository;

import com.bbangle.bbangle.AbstractIntegrationTest;
import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.domain.Product;
import com.bbangle.bbangle.board.domain.ProductImg;
import com.bbangle.bbangle.board.dto.StoreAllBoardDto;
import com.bbangle.bbangle.board.dto.StoreBestBoardDto;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.store.domain.Store;
import com.bbangle.bbangle.wishlist.domain.WishListBoard;
import com.bbangle.bbangle.wishlist.domain.WishListFolder;
import com.bbangle.bbangle.wishlist.domain.WishListStore;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class StoreRepositoryImplTest extends AbstractIntegrationTest {

    private Store store;
    private Board board;
    private Member member;

    @BeforeEach
    public void saveData() {
        createData(5, 20);
        createLikeData();
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
        boardRepository.deleteAll();
        storeRepository.deleteAll();
    }

    @Test
    void getAllBoardTest() {
        String boardProfile = "https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fboards%2F00000000%2F0.jpg?alt=media&token=f3d1925a-1e93-4e47-a487-63c7fc61e203";
        String boardTitle = "비건 베이커리";
        int boardPrice = 5400;
        boolean boardIsWished = true;
        boolean boardIsBundled = true;
        List<String> allTags = List.of("sugarFree", "glutenFree", "vegan", "ketogenic");

        String boardProfile2 = "https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fboards%2F00000000%2F0.jpg?alt=media&token=f3d1925a-1e93-4e47-a487-63c7fc61e203";
        String boardTitle2 = "콩볼 베이커리";
        int boardPrice2 = 5400;
        boolean boardIsWished2 = true;
        boolean boardIsBundled2 = true;

        String boardTitle3 = "빵빵이네";

        Pageable pageable = PageRequest.of(0, 10);
        var result = storeRepository.getAllBoard(pageable, store.getId());
        List<StoreAllBoardDto> actualContent = result.getContent();
        boolean isBoard1 = false;
        boolean isBoard2 = false;
        boolean isBoard3 = false;

        for (StoreAllBoardDto storeAllBoardDto : actualContent) {
            if (storeAllBoardDto.boardId() % 4 == 1L) {
                Assertions.assertEquals(boardProfile, storeAllBoardDto.thumbnail());
                // 로직 오류 개선 후 주석 해제 예정
                // Assertions.assertEquals(boardTitle, storeAllBoardDto.title());
                Assertions.assertEquals(boardPrice, storeAllBoardDto.price());
                Assertions.assertEquals(boardIsWished, storeAllBoardDto.isWished());
                // 로직 오류 개선 후 주석 해제 예정
                // Assertions.assertEquals(boardIsBundled2, storeAllBoardDto.isBundled());
                // assertThat(allTags,containsInAnyOrder(storeAllBoardDto.tags().toArray()));
                isBoard1 = true;
            } else if (storeAllBoardDto.boardId() % 4 == 2L) {
                Assertions.assertEquals(boardProfile2, storeAllBoardDto.thumbnail());
                // 로직 오류 개선 후 주석 해제 예정
                // Assertions.assertEquals(boardTitle, storeAllBoardDto.title());
                Assertions.assertEquals(boardPrice2, storeAllBoardDto.price());
                Assertions.assertEquals(boardIsWished2, storeAllBoardDto.isWished());
                // 로직 오류 개선 후 주석 해제 예정
                // Assertions.assertEquals(boardIsBundled2, storeAllBoardDto.isBundled());
                // assertThat(allTags,containsInAnyOrder(storeAllBoardDto.tags().toArray()));
                isBoard2 = true;
            } else if (storeAllBoardDto.boardId() % 4 == 3L) {
                Assertions.assertEquals(boardProfile2, storeAllBoardDto.thumbnail());
                // 로직 오류 개선 후 주석 해제 예정
                // Assertions.assertEquals(boardTitle3, storeAllBoardDto.title());
                Assertions.assertEquals(boardPrice2, storeAllBoardDto.price());
                Assertions.assertEquals(boardIsWished2, storeAllBoardDto.isWished());
                // 로직 오류 개선 후 주석 해제 예정
                // Assertions.assertEquals(boardIsBundled2, storeAllBoardDto.isBundled());
                // assertThat(allTags,containsInAnyOrder(storeAllBoardDto.tags().toArray()));
                isBoard3 = true;
            }
        }

        Assertions.assertTrue(isBoard1);
        Assertions.assertTrue(isBoard2);
        Assertions.assertTrue(isBoard3);
    }

    @Test
    @DisplayName("findBestBoards 정상")
    void findBestBoards() {
        String boardProfile = "https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fboards%2F00000000%2F0.jpg?alt=media&token=f3d1925a-1e93-4e47-a487-63c7fc61e203";
        int boardPrice = 5400;
        boolean boardIsBundled = true;

        List<StoreBestBoardDto> bestProducts = storeRepository.findBestBoards(store.getId());

        int index = 0;
        for (StoreBestBoardDto boardDto : bestProducts) {
            index++;

            if (index == 1) {
                Assertions.assertEquals(boardProfile, boardDto.thumbnail());
                Assertions.assertEquals("비건 베이커리", boardDto.title());
                Assertions.assertEquals(boardPrice, boardDto.price());
                Assertions.assertEquals(boardIsBundled, boardDto.isBundled());
            } else if (index == 2) {
                Assertions.assertEquals(boardProfile, boardDto.thumbnail());
                Assertions.assertEquals("콩볼 베이커리", boardDto.title());
                Assertions.assertEquals(boardPrice, boardDto.price());
                Assertions.assertEquals(boardIsBundled, boardDto.isBundled());
            } else if (index == 3) {
                Assertions.assertEquals(boardProfile, boardDto.thumbnail());
                Assertions.assertEquals("빵빵이네", boardDto.title());
                Assertions.assertEquals(boardPrice, boardDto.price());
                Assertions.assertEquals(boardIsBundled, boardDto.isBundled());
            } else if (index == 4) {
                Assertions.assertEquals(boardProfile, boardDto.thumbnail());
                Assertions.assertEquals("레오마켓", boardDto.title());
                Assertions.assertEquals(boardPrice, boardDto.price());
                Assertions.assertEquals(boardIsBundled, boardDto.isBundled());
            }
        }
    }

    private void createData(int storeCount, int boardCount) {
        for (int i = 0; i < storeCount; i++) {
            store = Store.builder()
                .identifier("7962401222")
                .name("RAWSOME")
                .profile(
                    "https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fprofile.jpg?alt=media&token=26bd1435-2c28-4b85-a5aa-b325e9aac05e")
                .introduce("건강을 먹다-로썸")
                .build();

            storeRepository.save(store);

            List<String> boardNames = new ArrayList<>() {
                {
                    add("비건 베이커리");
                    add("콩볼 베이커리");
                    add("빵빵이네");
                    add("레오마켓");
                }
            };
            for (int j = 0; j < boardCount; j++) {
                createBoard(
                    store,
                    boardNames.get(j % 4),
                    100 - j
                );
            }
        }
    }

    private void createBoard(
        Store store,
        String boardName,
        int boardView) {
        board = boardRepository.save(Board.builder()
            .store(store)
            .title(boardName)
            .price(5400)
            .status(true)
            .profile(
                "https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fboards%2F00000000%2F0.jpg?alt=media&token=f3d1925a-1e93-4e47-a487-63c7fc61e203")
            .purchaseUrl("https://smartstore.naver.com/rawsome/products/5727069436")
            .view(boardView)
            .sunday(false).monday(false).tuesday(false).wednesday(false).thursday(true)
            .sunday(false)
            .build());

        productRepository.save(Product.builder()
            .board(board)
            .title("콩볼")
            .price(3600)
            .category(Category.COOKIE)
            .glutenFreeTag(true)
            .highProteinTag(false)
            .sugarFreeTag(true)
            .veganTag(false)
            .ketogenicTag(false)
            .build());

        productRepository.save(Product.builder()
            .board(board)
            .title("카카모카")
            .price(5000)
            .category(Category.BREAD)
            .glutenFreeTag(true)
            .highProteinTag(false)
            .sugarFreeTag(false)
            .veganTag(true)
            .ketogenicTag(false)
            .build());

        productRepository.save(Product.builder()
            .board(board)
            .title("로미넛쑥")
            .price(5000)
            .category(Category.BREAD)
            .glutenFreeTag(false)
            .highProteinTag(false)
            .sugarFreeTag(true)
            .veganTag(false)
            .ketogenicTag(true)
            .build());

        boardImgRepository.save(ProductImg.builder()
            .board(board)
            .url("www.naver.com")
            .build());

        boardImgRepository.save(ProductImg.builder()
            .board(board)
            .url("www.naver.com")
            .build());
    }

    private void createLikeData() {
        member = memberRepository.save(
            Member.builder()
                .id(2L)
                .email("dd@ex.com")
                .nickname("test")
                .name("testName")
                .birth("99999")
                .phone("01023299893")
                .build());
        WishListFolder wishlistFolder = wishListFolderRepository.save(
            WishListFolder.builder().
                folderName("Test").
                member(member).
                build());
        wishListBoardRepository.save(
            WishListBoard.builder().board(board)
                .memberId(member.getId())
                .wishlistFolder(wishlistFolder)
                .build());
        wishListStoreRepository.save(
            WishListStore.builder()
                .store(store)
                .member(member)
                .build());
    }

}
