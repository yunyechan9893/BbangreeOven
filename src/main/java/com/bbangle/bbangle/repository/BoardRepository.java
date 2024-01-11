package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryDSLRepository{

}
