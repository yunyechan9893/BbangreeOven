package com.bbangle.bbangle.admin.repository;

import com.bbangle.bbangle.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBoardRepository extends JpaRepository<Board, Long>, AdminBoardQueryDSLRepository {


}
