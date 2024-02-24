package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.dto.BoardDto;
import com.bbangle.bbangle.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.model.*;
import com.bbangle.bbangle.repository.BoardRepository;
import com.bbangle.bbangle.repository.ProductRepository;
import com.bbangle.bbangle.repository.StoreRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@Transactional
@SpringBootTest
@Rollback
public class StoreRepositoryImplTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BoardImgRepository boardImgRepository;

    @BeforeEach
    public void saveData(){
        createData(5, 4);
    }

    @AfterEach
    void afterEach() {
        this.entityManager
                .createNativeQuery("ALTER TABLE store ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();

        this.entityManager
                .createNativeQuery("ALTER TABLE product_board ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();

        this.entityManager
                .createNativeQuery("ALTER TABLE member ALTER COLUMN `id` RESTART WITH 2")
                .executeUpdate();
    }

    @Test
    public void getStoreDetailResponseDtoTest(){
        String storeName = "RAWSOME";
        String storeProfile = "https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fprofile.jpg?alt=media&token=26bd1435-2c28-4b85-a5aa-b325e9aac05e";
        boolean storeIsWished = true;
        String storeIntroduce = "건강을 먹다-로썸";

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

        Long storeId = Long.valueOf(1);
        StoreDetailResponseDto result = storeRepository.getStoreDetailResponseDto(storeId);

        var store = result.store();
        Assertions.assertEquals(storeName, store.storeName());
        Assertions.assertEquals(storeProfile, store.profile());
        Assertions.assertEquals(storeIsWished, store.isWished());
        Assertions.assertEquals(storeIntroduce, store.introduce());

        boolean isBoard1 = false;
        boolean isBoard2 = false;
        boolean isBoard3 = false;
        var allProducts = result.allProducts();
        for (BoardDto boardDto:allProducts) {
            if (boardDto.boardId() == 1L) {
                Assertions.assertEquals(boardProfile, boardDto.thumbnail());
                Assertions.assertEquals(boardTitle, boardDto.title());
                Assertions.assertEquals(boardPrice, boardDto.price());
                Assertions.assertEquals(boardIsWished, boardDto.isWished());
                Assertions.assertEquals(boardIsBundled, boardDto.isBundled());
                assertThat(allTags,containsInAnyOrder(boardDto.tags().toArray()));
                isBoard1 = true;
            } else if (boardDto.boardId() == 2L) {
                Assertions.assertEquals(boardProfile2, boardDto.thumbnail());
                Assertions.assertEquals(boardTitle2, boardDto.title());
                Assertions.assertEquals(boardPrice2, boardDto.price());
                Assertions.assertEquals(boardIsWished2, boardDto.isWished());
                Assertions.assertEquals(boardIsBundled2, boardDto.isBundled());
                assertThat(allTags,containsInAnyOrder(boardDto.tags().toArray()));
                isBoard2 = true;
            } else if (boardDto.boardId() == 3L) {
                Assertions.assertEquals(boardProfile2, boardDto.thumbnail());
                Assertions.assertEquals(boardTitle3, boardDto.title());
                Assertions.assertEquals(boardPrice2, boardDto.price());
                Assertions.assertEquals(boardIsWished2, boardDto.isWished());
                Assertions.assertEquals(boardIsBundled2, boardDto.isBundled());
                assertThat(allTags,containsInAnyOrder(boardDto.tags().toArray()));
                isBoard3 = true;
            }
        }

        Assertions.assertEquals(true, isBoard1);
        Assertions.assertEquals(true, isBoard2);
        Assertions.assertEquals(true, isBoard3);



        int index = 0;
        var bestProducts = result.bestProducts();
        for (BoardDto boardDto:bestProducts) {
            index ++;

            if (index == 1) {
                Assertions.assertEquals(1L, boardDto.boardId());
                Assertions.assertEquals(boardProfile, boardDto.thumbnail());
                Assertions.assertEquals("비건 베이커리", boardDto.title());
                Assertions.assertEquals(boardPrice, boardDto.price());
                Assertions.assertEquals(boardIsWished, boardDto.isWished());
                Assertions.assertEquals(boardIsBundled, boardDto.isBundled());
                assertThat(allTags, containsInAnyOrder(boardDto.tags().toArray()));
            } else if (index == 2) {
                Assertions.assertEquals(2L, boardDto.boardId());
                Assertions.assertEquals(boardProfile, boardDto.thumbnail());
                Assertions.assertEquals("콩볼 베이커리", boardDto.title());
                Assertions.assertEquals(boardPrice, boardDto.price());
                Assertions.assertEquals(boardIsWished, boardDto.isWished());
                Assertions.assertEquals(boardIsBundled, boardDto.isBundled());
                assertThat(allTags, containsInAnyOrder(boardDto.tags().toArray()));
            } else if (index == 3) {
                Assertions.assertEquals(3L, boardDto.boardId());
                Assertions.assertEquals(boardProfile, boardDto.thumbnail());
                Assertions.assertEquals("빵빵이네", boardDto.title());
                Assertions.assertEquals(boardPrice, boardDto.price());
                Assertions.assertEquals(boardIsWished, boardDto.isWished());
                Assertions.assertEquals(boardIsBundled, boardDto.isBundled());
                assertThat(allTags, containsInAnyOrder(boardDto.tags().toArray()));
            } else if (index == 4) {
                Assertions.assertEquals(4L, boardDto.boardId());
                Assertions.assertEquals(boardProfile, boardDto.thumbnail());
                Assertions.assertEquals("레오마켓", boardDto.title());
                Assertions.assertEquals(boardPrice, boardDto.price());
                Assertions.assertEquals(boardIsWished, boardDto.isWished());
                Assertions.assertEquals(boardIsBundled, boardDto.isBundled());
                assertThat(allTags, containsInAnyOrder(boardDto.tags().toArray()));
            }
        }
    }

    private void createData(int storeCount, int boardCount){
        for(int i=0; i<storeCount; i++){
            var store = Store.builder()
                    .identifier("7962401222")
                    .name("RAWSOME")
                    .profile("https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fprofile.jpg?alt=media&token=26bd1435-2c28-4b85-a5aa-b325e9aac05e")
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
            for(int j=0; j<boardCount; j++){
                createBoard(
                        store,
                        boardNames.get(j),
                        100-j
                        );
            }
        }
    }

    private void createBoard(
            Store store,
            String boardName,
            int boardView){
        var board = Board.builder()
                .store(store)
                .title(boardName)
                .price(5400)
                .status(true)
                .profile("https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fboards%2F00000000%2F0.jpg?alt=media&token=f3d1925a-1e93-4e47-a487-63c7fc61e203")
                .detail("test.txt")
                .purchaseUrl("https://smartstore.naver.com/rawsome/products/5727069436")
                .view(boardView)
                .sunday(false).monday(false).tuesday(false).wednesday(false).thursday(true).sunday(false)
                .build();

        var product1 = Product.builder()
                .board(board)
                .title("콩볼")
                .price(3600)
                .category(Category.COOKIE)
                .glutenFreeTag(true)
                .highProteinTag(false)
                .sugarFreeTag(true)
                .veganTag(false)
                .ketogenicTag(false)
                .build();

        var product2 = Product.builder()
                .board(board)
                .title("카카모카")
                .price(5000)
                .category(Category.BREAD)
                .glutenFreeTag(true)
                .highProteinTag(false)
                .sugarFreeTag(false)
                .veganTag(true)
                .ketogenicTag(false)
                .build();

        var product3 = Product.builder()
                .board(board)
                .title("로미넛쑥")
                .price(5000)
                .category(Category.BREAD)
                .glutenFreeTag(false)
                .highProteinTag(false)
                .sugarFreeTag(true)
                .veganTag(false)
                .ketogenicTag(true)
                .build();

        var boardImg = ProductImg.builder()
                .board(board)
                .url("www.naver.com")
                .build();

        var boardImg2 = ProductImg.builder()
                .board(board)
                .url("www.naver.com")
                .build();

        boardRepository.save(board);
        boardImgRepository.save(boardImg);
        boardImgRepository.save(boardImg2);
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }
}
