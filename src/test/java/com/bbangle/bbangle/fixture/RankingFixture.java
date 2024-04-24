package com.bbangle.bbangle.fixture;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.ranking.domain.Ranking;

public class RankingFixture {

    public static Ranking newRanking(Board board) {
        return Ranking.builder()
            .board(board)
            .recommendScore(0.0)
            .popularScore(0.0)
            .build();
    }

}
