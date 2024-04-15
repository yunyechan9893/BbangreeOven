package com.bbangle.bbangle.ranking.service;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.repository.BoardRepository;
import com.bbangle.bbangle.ranking.domain.Ranking;
import com.bbangle.bbangle.ranking.repository.RankingRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final BoardRepository boardRepository;

    public void updatingNonRankedBoards() {
        List<Board> unRankedBoards = boardRepository.checkingNullRanking();
        List<Ranking> rankings = new ArrayList<>();
        unRankedBoards.stream()
            .map(board -> Ranking.builder()
                .board(board)
                .popularScore(0L)
                .recommendScore(0L)
                .build())
            .forEach(rankings::add);
        rankingRepository.saveAll(rankings);
    }

}
