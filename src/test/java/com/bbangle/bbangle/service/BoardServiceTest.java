package com.bbangle.bbangle.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.model.Board;
import com.bbangle.bbangle.model.Category;
import com.bbangle.bbangle.model.Product;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.repository.BoardRepository;
import com.bbangle.bbangle.repository.ProductRepository;
import com.bbangle.bbangle.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BoardService boardService;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        boardRepository.deleteAll();
        storeRepository.deleteAll();
        Store store = Store.builder()
            .identifier("identifier")
            .name("name")
            .introduce("introduce")
            .profile("profile")
            .isDeleted(false)
            .build();

        storeRepository.save(store);

        Board board = Board.builder()
            .store(store)
            .title("title")
            .price(1000)
            .status(true)
            .profile("profile")
            .detail("detail")
            .purchaseUrl("purchaseUrl")
            .view(1)
            .sunday(true)
            .monday(true)
            .tuesday(true)
            .wednesday(true)
            .thursday(true)
            .friday(true)
            .saturday(true)
            .isDeleted(false)
            .build();

        Board board2 = Board.builder()
            .store(store)
            .title("title")
            .price(1000)
            .status(true)
            .profile("profile")
            .detail("detail")
            .purchaseUrl("purchaseUrl")
            .view(1)
            .sunday(true)
            .monday(true)
            .tuesday(true)
            .wednesday(true)
            .thursday(true)
            .friday(true)
            .saturday(true)
            .isDeleted(false)
            .build();
        boardRepository.save(board);
        boardRepository.save(board2);

        Product product = Product.builder()
            .board(board)
            .title("title")
            .price(1000)
            .category(Category.BREAD)
            .glutenFreeTag(true)
            .highProteinTag(true)
            .sugarFreeTag(true)
            .veganTag(true)
            .ketogenicTag(true)
            .build();

        Product product2 = Product.builder()
            .board(board2)
            .title("title")
            .price(1000)
            .category(Category.BREAD)
            .glutenFreeTag(false)
            .highProteinTag(true)
            .sugarFreeTag(true)
            .veganTag(true)
            .ketogenicTag(false)
            .build();

        Product product3 = Product.builder()
            .board(board2)
            .title("title")
            .price(1000)
            .category(Category.BREAD)
            .glutenFreeTag(true)
            .highProteinTag(true)
            .sugarFreeTag(false)
            .veganTag(true)
            .ketogenicTag(false)
            .build();

        productRepository.save(product);
        productRepository.save(product2);
        productRepository.save(product3);
    }


    @Test
    @DisplayName("모든 리스트를 정상적으로 조회한다.")
    public void showAllList() throws Exception {
        //given, when
        String sort = "";
        List<BoardResponseDto> boardList = boardService.getBoardList(sort);
        BoardResponseDto response1 = boardList.get(0);
        BoardResponseDto response2 = boardList.get(1);

        //then
        assertThat(boardList).hasSize(2);

        assertThat(response1.tagDto().glutenFreeTag()).isEqualTo(true);
        assertThat(response1.tagDto().highProteinTag()).isEqualTo(true);
        assertThat(response1.tagDto().sugarFreeTag()).isEqualTo(true);
        assertThat(response1.tagDto().veganTag()).isEqualTo(true);
        assertThat(response1.tagDto().ketogenicTag()).isEqualTo(true);

        assertThat(response2.tagDto().glutenFreeTag()).isEqualTo(true);
        assertThat(response2.tagDto().highProteinTag()).isEqualTo(true);
        assertThat(response2.tagDto().sugarFreeTag()).isEqualTo(true);
        assertThat(response2.tagDto().veganTag()).isEqualTo(true);
        assertThat(response2.tagDto().ketogenicTag()).isEqualTo(false);

    }

}
