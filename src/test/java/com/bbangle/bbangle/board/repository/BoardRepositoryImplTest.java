package com.bbangle.bbangle.board.repository;

import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.dto.ProductDto;
import com.bbangle.bbangle.testutil.TestFactoryManager;
import com.bbangle.bbangle.member.repository.MemberRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import com.bbangle.bbangle.store.repository.StoreRepository;
import com.bbangle.bbangle.testutil.model.*;
import com.bbangle.bbangle.wishListBoard.repository.WishListProductRepository;
import com.bbangle.bbangle.wishListFolder.repository.WishListFolderRepository;
import com.bbangle.bbangle.wishListStore.repository.WishListStoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback
public class BoardRepositoryImplTest {
    private final TestFactoryManager testFactoryManager;
    private final Long memberId = 2L;

    public BoardRepositoryImplTest(
            @Autowired MemberRepository memberRepository,
            @Autowired StoreRepository storeRepository,
            @Autowired BoardRepository boardRepository,
            @Autowired ProductRepository productRepository,
            @Autowired EntityManager entityManager,
            @Autowired BoardImgRepository boardImgRepository,
            @Autowired WishListFolderRepository wishListFolderRepository,
            @Autowired WishListProductRepository wishListProductRepository,
            @Autowired WishListStoreRepository wishListStoreRepository
    ){
        testFactoryManager = new TestFactoryManager(entityManager)
                .setTestStoreFactory(storeRepository)
                .setTestBoardFactory(boardRepository)
                .setTestProductFactory(productRepository)
                .setTestBoardImageFactory(boardImgRepository)
                .setTestMemberFactory(memberRepository)
                .setTestWishlistFolderFactory(wishListFolderRepository)
                .setTestWishlistBoardFactory(wishListProductRepository)
                .setTestWishlistStoreFactory(wishListStoreRepository);
    }

    @BeforeEach
    public void saveData() {
        createProductRelatedContent(10);
        createLikeData();
    }

    @AfterEach
    void afterEach() {
        testFactoryManager.resetAutoIncreasementAndRowData();
    }

    @Test
    public void getBoardResponseDtoTest(){
        var boardRepository = testFactoryManager.getTestBoardFactory().getRepository();
        var board = testFactoryManager.getTestBoardFactory().getTestEntity("board0");

        var result = boardRepository.getBoardDetailResponse(memberId, board.getId());

        Assertions.assertEquals(1L,result.store().storeId());
        Assertions.assertEquals("RAWSOME",result.store().storeName());
        Assertions.assertEquals("비건 베이커리 로썸 비건빵",result.board().title());
        assertThat(List.of("glutenFree", "sugarFree", "vegan", "ketogenic"),containsInAnyOrder(result.board().tags().toArray()));

        boolean isProduct1 = false;
        boolean isProduct2 = false;
        boolean isProduct3 = false;
        for (ProductDto productDto:
            result.board().products()) {
                switch (productDto.title()){
                    case "콩볼":
                        assertThat(List.of("glutenFree", "sugarFree", "vegan", "ketogenic"),containsInAnyOrder(productDto.tags().toArray()));
                        isProduct1 = true;
                        break;
                    case "카카모카":
                        assertThat(List.of("glutenFree", "vegan"),containsInAnyOrder(productDto.tags().toArray()));
                        isProduct2 = true;
                        break;
                    case "로미넛쑥":
                        assertThat(List.of("glutenFree", "sugarFree", "vegan"),containsInAnyOrder(productDto.tags().toArray()));
                        isProduct3 = true;
                        break;
                }
        }

        Assertions.assertEquals(true, isProduct1);
        Assertions.assertEquals(true, isProduct2);
        Assertions.assertEquals(true, isProduct3);
    }

    @Test
    @DisplayName("Wished Product 테이블에 값들이 존재해도, 내 데이터가 아니면 isWished는 false가 된다")
    public void getBoardResponseDtoLikeTest(){
        var boardRepository = testFactoryManager.getTestBoardFactory().getRepository();
        var board = testFactoryManager.getTestBoardFactory().getTestEntity("board1");
        var result = boardRepository.getBoardDetailResponse(memberId, board.getId());
       System.out.println(result.store().storeId());
        Assertions.assertEquals(false, result.store().isWished(), "스토어 Like가 false 입니다");
        Assertions.assertEquals(true, result.board().isWished(), "보드 Like가 false 입니다");
    }

    @Test
    @DisplayName("Wished Productm isWished는 true가 된다")
    public void getBoardLikeTrueTest(){
        var boardRepository = testFactoryManager.getTestBoardFactory().getRepository();
        var board = testFactoryManager.getTestBoardFactory().getTestEntity("board1");
        var result = boardRepository.getBoardDetailResponse(memberId, board.getId());

        Assertions.assertEquals(false, result.store().isWished(), "스토어 Like가 true 입니다");
        Assertions.assertEquals(true, result.board().isWished(), "보드 Like가 true 입니다");
    }


    private void createProductRelatedContent(int count) {
        for (int i = 0; i < count; i++) {
            var store = testFactoryManager.getTestStoreFactory().pushTestEntity(
                    "store" + i,
                    new TestStore().setIdentifier("7962401222")
                        .setName("RAWSOME")
                        .setProfile("https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fprofile.jpg?alt=media&token=26bd1435-2c28-4b85-a5aa-b325e9aac05e")
                        .getModel()
            );

            var board = testFactoryManager.getTestBoardFactory().pushTestEntity(
                    "board" + i,
                    new TestBoard(store)
                            .setBoardName("비건 베이커리 로썸 비건빵")
                            .setPrice(5400)
                            .setStatus(true)
                            .setProfile("https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fboards%2F00000000%2F0.jpg?alt=media&token=f3d1925a-1e93-4e47-a487-63c7fc61e203")
                            .setPurchaseUrl("https://smartstore.naver.com/rawsome/products/5727069436")
                            .setView(100)
                            .setThursday(true)
                            .setSaturday(true)
                            .getModel()
            );

            testFactoryManager.getTestProductFactory().pushTestEntity(
                    "product" + (i * 3 - 2),
                    new TestProduct(board)
                            .setProductName("콩볼")
                            .setPrice(3600)
                            .setCategory(Category.COOKIE)
                            .setGlutenFreeTag(true)
                            .setSugarFreeTag(true)
                            .setVeganTag(true)
                            .setKetogenicTag(true)
                            .getModel()
            );

            testFactoryManager.getTestProductFactory().pushTestEntity(
                    "product" + (i * 3 - 1),
                    new TestProduct(board)
                            .setProductName("카카모카")
                            .setPrice(5000)
                            .setCategory(Category.BREAD)
                            .setGlutenFreeTag(true)
                            .setVeganTag(true)
                            .getModel()
            );

            testFactoryManager.getTestProductFactory().pushTestEntity(
                    "product" + (i * 3),
                    new TestProduct(board)
                            .setProductName("로미넛쑥")
                            .setPrice(5000)
                            .setCategory(Category.BREAD)
                            .setGlutenFreeTag(true)
                            .setSugarFreeTag(true)
                            .setVeganTag(true)
                            .getModel()
            );

            testFactoryManager.getTestBoardImageFactory().pushTestEntity(
                    "boardImg" + (i * 2 - 1),
                    new TestBoardImg(board)
                            .setImageUrl("www.naver.com")
                            .getModel());

            testFactoryManager.getTestBoardImageFactory().pushTestEntity(
                    "boardImg" + (i * 2),
                    new TestBoardImg(board)
                            .setImageUrl("www.naver.com")
                            .getModel());
        }
    }

    private void createLikeData(){
        var member = testFactoryManager.getTestMemberFactory().pushTestEntity(
                "member1",
                new TestMember()
                        .setId(memberId)
                        .getModel()
        );

        var wishlistFolder = testFactoryManager.getTestWishlistFolderFactory().pushTestEntity(
                "wishlistFolder1",
                new TestWishlistFolder(member).getModel()
        );

        var board = testFactoryManager.getTestBoardFactory().getTestEntity("board1");
        testFactoryManager.getTestWishlistBoardFactory().pushTestEntity(
                "wishlistBoard1",
                new TestWishlistBoard(wishlistFolder)
                        .setMemberId(memberId)
                        .setBoard(board)
                        .getModel()
        );

        var store = testFactoryManager.getTestStoreFactory().getTestEntity("store1");
        testFactoryManager.getTestWishlistStoreFactory().pushTestEntity(
                "wishlistStore1",
                new TestWishlistStore(store, member).getModel()
        );
    }

}
