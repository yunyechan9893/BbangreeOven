package com.bbangle.bbangle.board.repository;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.dto.BoardAllTitleDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryDSLRepository {

    @Query("SELECT b.id, b.title FROM Board b")
    List<BoardAllTitleDto> findTitleByBoardAll();
}
