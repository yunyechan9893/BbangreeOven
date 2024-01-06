package com.bbangle.bbangle.repository;

import java.util.List;
import com.bbangle.bbangle.dto.BoardResponseDto;

public interface BoardQueryDSLRepository {
    List<BoardResponseDto> getBoardResponseDto();

}
