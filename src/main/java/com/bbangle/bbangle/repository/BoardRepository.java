package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository
    extends JpaRepository<Board, Long>,
    BoardQueryDSLRepository{

}
