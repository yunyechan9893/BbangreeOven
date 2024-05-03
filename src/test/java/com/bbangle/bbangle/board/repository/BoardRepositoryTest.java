package com.bbangle.bbangle.board.repository;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.domain.Product;
import com.bbangle.bbangle.board.domain.ProductImg;
import com.bbangle.bbangle.board.dto.ProductDto;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.store.domain.Store;
import com.bbangle.bbangle.member.repository.MemberRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import com.bbangle.bbangle.store.repository.StoreRepository;

import com.bbangle.bbangle.wishList.domain.WishListFolder;
import com.bbangle.bbangle.wishList.domain.WishListBoard;
import com.bbangle.bbangle.wishList.domain.WishListStore;
import com.bbangle.bbangle.wishList.repository.WishListFolderRepository;
import com.bbangle.bbangle.wishList.repository.WishListBoardRepository;
import com.bbangle.bbangle.wishList.repository.WishListStoreRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import static org.hamcrest.Matchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BoardRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    BoardImgRepository boardImgRepository;
    @Autowired
    WishListFolderRepository wishListFolderRepository;
    @Autowired
    WishListBoardRepository wishlistBoardRepository;
    @Autowired
    WishListStoreRepository wishListStoreRepository;
    Store store;
    Board board;
    Member member;

    @BeforeEach
    public void saveEntity() {
        createMember();
        createProductRelatedContent();
        createWishlist();
    }

    @AfterEach
    public void deleteAllEntity(){
        memberRepository.deleteAll();
        productRepository.deleteAll();
        boardRepository.deleteAll();
        storeRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 게시물 상세보기 조회가 잘되고 있다")
    public void getProductDtosToDuplicatedTagsTest(){
        List<String> productOneTag = new ArrayList<>();
        productOneTag.add("glutenFree");
        productOneTag.add("sugarFree");

        List<String> productTwoTag = new ArrayList<>();
        productOneTag.add("highProtein");
        productOneTag.add("sugarFree");

        List<String> productThreeTag = new ArrayList<>();
        productOneTag.add("glutenFree");
        productOneTag.add("ketogenic");

        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(ProductDto.builder().tags(productOneTag).build());
        productDtos.add(ProductDto.builder().tags(productTwoTag).build());
        productDtos.add(ProductDto.builder().tags(productThreeTag).build());

        List<String> actualDuplicatedTags = boardRepository.getProductDtosToDuplicatedTags(productDtos);

        assertThat(actualDuplicatedTags, containsInAnyOrder("glutenFree", "sugarFree", "highProtein", "ketogenic"));
        assertThat(actualDuplicatedTags, not(hasItem("vegan")));
    }

    @Test
    @DisplayName("상품 게시물 상세보기 조회가 잘되고 있다")
    public void getBoardResponseDtoTest(){
        var boardDetailResponse = boardRepository.getBoardDetailResponse(member.getId(), board.getId());

        var firstBoard = boardDetailResponse.board();
        assertThat(firstBoard.title(), is("비건 베이커리 로썸 비건빵"));
        assertThat(firstBoard.isBundled(), is(true));
        assertThat(firstBoard.tags(), containsInAnyOrder("glutenFree", "sugarFree", "vegan", "ketogenic"));

        var images  = firstBoard.images();
        assertThat(images.toArray(), arrayWithSize(2));

        var firstImage = images.get(0);
        assertThat(firstImage.url(), is("test.jpg"));

        var products = boardDetailResponse.board().products();
        assertThat(products.toArray(), arrayWithSize(3));

        var firstProduct = boardDetailResponse.board().products().get(0);

        assertThat(firstProduct.title(), is("콩볼"));

        var firstProductTag = firstProduct.tags().stream().toArray();
        assertThat(firstProductTag, arrayContaining("glutenFree", "sugarFree", "vegan", "ketogenic"));
    }

    @Test
    @DisplayName("Wished Product 테이블에 값들이 존재해도, 내 데이터가 아니면 isWished는 false가 된다")
    public void getBoardResponseDtoLikeTest(){
        var boardDetailResponse = boardRepository.getBoardDetailResponse(member.getId(), board.getId());

        assertThat(boardDetailResponse.store().isWished(), is(false));
        assertThat(boardDetailResponse.board().isWished(), is(false));
    }

    @Test
    @DisplayName("Wished Productm isWished는 true가 된다")
    public void getBoardLikeTrueTest(){
        var boardDetailResponse = boardRepository.getBoardDetailResponse(member.getId(), board.getId());

        assertThat(boardDetailResponse.store().isWished(), is(false));
        assertThat(boardDetailResponse.board().isWished(), is(false));
    }

    private void createProductRelatedContent() {
            store = storeRepository.save(
                    Store.builder()
                            .identifier("7962401222")
                            .name("RAWSOME")
                            .profile("https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fprofile.jpg?alt=media&token=26bd1435-2c28-4b85-a5aa-b325e9aac05e")
                            .build());

            board = boardRepository.save(
                    Board.builder()
                            .store(store)
                            .title("비건 베이커리 로썸 비건빵")
                            .price(5400)
                            .status(true)
                            .profile("https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fboards%2F00000000%2F0.jpg?alt=media&token=f3d1925a-1e93-4e47-a487-63c7fc61e203")
                            .purchaseUrl("https://smartstore.naver.com/rawsome/products/5727069436")
                            .view(100)
                            .sunday(true)
                            .monday(true)
                            .tuesday(true)
                            .wednesday(true)
                            .thursday(true)
                            .friday(true)
                            .saturday(true)
                            .build());

            productRepository.saveAll(List.of(
                    Product.builder()
                            .board(board)
                            .title("콩볼")
                            .price(3600)
                            .category(Category.COOKIE)
                            .glutenFreeTag(true)
                            .sugarFreeTag(true)
                            .veganTag(true)
                            .ketogenicTag(true)
                            .build(),
                    Product.builder()
                            .board(board)
                            .title("카카모카")
                            .price(5000)
                            .category(Category.BREAD)
                            .glutenFreeTag(true)
                            .veganTag(true)
                            .build(),
                    Product.builder()
                            .board(board)
                            .title("로미넛쑥")
                            .price(5000)
                            .category(Category.BREAD)
                            .glutenFreeTag(true)
                            .sugarFreeTag(true)
                            .veganTag(true)
                            .build()
            ));

            boardImgRepository.saveAll(List.of(ProductImg.builder()
                            .board(board)
                            .url("test.jpg")
                            .build(),
                    ProductImg.builder()
                            .board(board)
                            .url("test.jpg")
                            .build()));
    }
    private void createMember() {
        member = memberRepository.save(Member.builder()
                .id(2L)
                .build());
    }

    private void createWishlist(){
        wishListStoreRepository.save(WishListStore.builder()
                        .member(member)
                        .store(store)
                        .isDeleted(false)
                        .build());

        var wishlistFolder = wishListFolderRepository.save(
                WishListFolder.builder()
                        .member(member)
                        .isDeleted(false)
                        .build());

        wishlistBoardRepository.save(WishListBoard.builder()
                .memberId(member.getId())
                .wishlistFolder(wishlistFolder)
                .board(board)
                .isDeleted(false)
                .build());
    }
}
