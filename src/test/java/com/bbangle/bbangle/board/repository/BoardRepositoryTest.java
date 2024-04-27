package com.bbangle.bbangle.board.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.bbangle.bbangle.AbstractIntegrationTest;
import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.ranking.domain.Ranking;
import java.util.HashMap;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardRepositoryTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("getAllBoardTitle 정상 확인")
    void getAllBoardTitle() {
        Board board1 = fixtureBoard();
        Board board2 = fixtureBoard();

        HashMap<Long, String> result = boardRepository.getAllBoardTitle();

        // then
        assertThat(result).containsEntry(board1.getId(), board1.getTitle());
        assertThat(result).containsEntry(board2.getId(), board2.getTitle());
    }

    @Test
    @DisplayName("checkingNullRanking 정상 확인")
    void checkingNullRanking() {
        // given
        Board fixtureBoard = fixtureBoard();
        Ranking targetranking = fixtureMonkey.giveMeBuilder(Ranking.class)
            .set("board", fixtureBoard)
            .sample();
        Ranking nonTargetRanking = fixtureMonkey.giveMeBuilder(Ranking.class)
            .set("board", null)
            .sample();
        rankingRepository.saveAll(Lists.newArrayList(targetranking, nonTargetRanking));

        // when
        List<Board> result = boardRepository.checkingNullRanking();

        assertThat(result).hasSize(1);
    }

}
