package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.model.Board;
import com.bbangle.bbangle.model.Category;
import com.bbangle.bbangle.model.Product;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.repository.*;
import com.bbangle.bbangle.service.SearchService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.Random;

@SpringBootTest
@Transactional
@Rollback
public class BoardRepositoryImplTest {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void saveData(){
        createData(15);
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
    public void getBoardResponseDtoTest(){
        Long boardId = Long.valueOf(1);
        boardRepository.getBoardDetailResponseDto(boardId);
    }

    @Test
    public void updateBoardDetailTest(){
        String defaultURL = "https://bbangree-oven.cdn.ntruss.com";
        String storeId = "1";
        String boardId = "1";
        String fileName = "detail.html";
        String filePath = String.format("%s/%s/%s/%s", defaultURL,storeId,boardId,fileName);

        var result = boardRepository.updateDetailWhereStoreIdEqualsBoardId(
                Long.parseLong(boardId),
                filePath
        );

        Assertions.assertEquals(1, result);

        Optional<Board> resultBoard = boardRepository.findById(Long.parseLong(boardId));

        resultBoard.stream().forEach(
                board -> {
                    Assertions.assertEquals(Long.parseLong(boardId), board.getId());
                    Assertions.assertEquals(filePath, board.getDetail());
                }
        );
    }

    private void createData(int count){
        for(int i=0; i<count; i++){
            var store = Store.builder()
                    .identifier("7962401222")
                    .name("RAWSOME")
                    .profile("https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fprofile.jpg?alt=media&token=26bd1435-2c28-4b85-a5aa-b325e9aac05e")
                    .introduce("건강을 먹다-로썸")
                    .build();

            var board = Board.builder()
                    .store(store)
                    .title("비건 베이커리 로썸 비건빵")
                    .price(5400)
                    .status(true)
                    .profile("https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fboards%2F00000000%2F0.jpg?alt=media&token=f3d1925a-1e93-4e47-a487-63c7fc61e203")
                    .detail("test.txt")
                    .purchaseUrl("https://smartstore.naver.com/rawsome/products/5727069436")
                    .view(100)
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
                    .veganTag(true)
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
                    .glutenFreeTag(true)
                    .highProteinTag(false)
                    .sugarFreeTag(false)
                    .veganTag(true)
                    .ketogenicTag(false)
                    .build();

            storeRepository.save(store);
            boardRepository.save(board);
            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
        }
    }
}
