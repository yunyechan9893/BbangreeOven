package com.bbangle.bbangle.ranking.repository;

import com.bbangle.bbangle.ranking.domain.Ranking;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    Optional<Ranking> findByBoardId(Long boardId);

}
