package com.bbangle.bbangle.board.repository;

import com.bbangle.bbangle.board.dto.BoardDetailDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardDetailQueryDSLRepository {

    List<BoardDetailDto> findByBoardId(Long boardId);
}
