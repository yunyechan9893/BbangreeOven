package com.bbangle.bbangle.board.repository;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.bbangle.bbangle.AbstractIntegrationTest;
import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.domain.Product;
import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.board.dto.BoardAllTitleDto;
import com.bbangle.bbangle.board.dto.BoardAndImageDto;
import com.bbangle.bbangle.board.dto.ProductDto;
import com.bbangle.bbangle.ranking.domain.Ranking;
import com.querydsl.core.Tuple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardRepositoryTest extends AbstractIntegrationTest {

    private final QBoard board = QBoard.board;
    private final String TEST_TITLE = "TestTitle";

    @Test
    @DisplayName("스토어 상세페이지 - 게시판, 이미지 조회 기능 : 게시판 아이디로 게시판, 이미지를 조회할 수 있다")
    void getBoardAndImages() {
        Board targetBoard = fixtureBoard(Map.of("title", TEST_TITLE));
        fixtureBoardImage(Map.of("board", targetBoard));
        fixtureBoardImage(Map.of("board", targetBoard));

        List<BoardAndImageDto> boardAndImageDtos = boardRepository
            .findBoardAndBoardImageByBoardId(targetBoard.getId());

        assertThat(boardAndImageDtos).hasSize(2);

        BoardAndImageDto boardAndImageDto = boardAndImageDtos.stream().findFirst().get();
        assertThat(boardAndImageDto.id()).isNotNull();
        assertThat(boardAndImageDto.profile()).isNotNull();
        assertThat(boardAndImageDto.title()).isNotNull();
        assertThat(boardAndImageDto.price()).isNotNull();
        assertThat(boardAndImageDto.purchaseUrl()).isNotNull();
        assertThat(boardAndImageDto.deliveryFee()).isNotNull();
        assertThat(boardAndImageDto.freeShippingConditions()).isNotNull();
    }

    @Test
    @DisplayName("스토어 상세페이지 - 게시판 상세 이미지 조회 기능 : 게시판 아이디로 상세 이미지를 조회할 수 있다")
    void getBoardDetailTest() {
        Board targetBoard = fixtureBoard(Map.of("title", TEST_TITLE));
        fixtureBoardDetail(Map.of("board", targetBoard));
        fixtureBoardDetail(Map.of("board", targetBoard));

        List<String> boardDetailDtos = boardDetailRepository
            .findByBoardId(targetBoard.getId());

        assertThat(boardDetailDtos).hasSize(2);

        assertThat(boardDetailDtos).isNotEmpty();
    }

    @Test
    @DisplayName("스토어 상세페이지 - 상품 조회 기능 : 게시판 아이디로 상품리스트를 조회할 수 있다")
    void getProductDtoTest() {
        Map<String, Object> productParam = new HashMap<>();
        productParam.put("title", TEST_TITLE);
        productParam.put("category", Category.BREAD);

        List<Product> products = List.of(
            fixtureProduct(productParam),
            fixtureProduct(productParam),
            fixtureProduct(productParam));

        Board targetBoard = fixtureBoard(Map.of("productList", products));

        List<ProductDto> productDtos = boardRepository.getProductDto(targetBoard.getId());

        assertThat(productDtos.size()).isEqualTo(3);
        productDtos.forEach(productDto -> {
            assertThat(productDto.productId()).isNotNull();
            assertThat(productDto.productTitle()).isNotNull();
            assertThat(productDto.category()).isNotNull();
            assertThat(productDto.glutenFreeTag()).isNotNull();
            assertThat(productDto.highProteinTag()).isNotNull();
            assertThat(productDto.sugarFreeTag()).isNotNull();
            assertThat(productDto.veganTag()).isNotNull();
            assertThat(productDto.ketogenicTag()).isNotNull();
        });
    }

    @Test
    @DisplayName("게시판 전체의 아이디와 게시글명을 가져올 수 있다.")
    void getAllBoardTitle() {
        fixtureBoard(Map.of("title", TEST_TITLE));
        fixtureBoard(Map.of("title", TEST_TITLE));
        List<BoardAllTitleDto> boardAllTitleDtos = boardRepository.findTitleByBoardAll();

        assertThat(boardAllTitleDtos.size()).isEqualTo(2);
        boardAllTitleDtos.forEach(boardAllTitleDto -> {
            assertThat(boardAllTitleDto.boardId()).isNotNull();
            assertThat(boardAllTitleDto.Title()).isNotNull();
        });

    }

    @Test
    @DisplayName("checkingNullRanking 정상 확인")
    void checkingNullRanking() {
        // given
        Board fixtureBoard = fixtureBoard(emptyMap());
        Board fixtureBoard2 = fixtureBoard(emptyMap());
        Ranking target = fixtureRanking(Map.of("board", fixtureBoard));
        Ranking nonTarget = fixtureRanking(Map.of("board", fixtureBoard2));

        nonTarget.setBoard(null);
        rankingRepository.save(nonTarget);

        // when
        List<Board> result = boardRepository.checkingNullRanking();

        assertThat(result).hasSize(1);
    }
}