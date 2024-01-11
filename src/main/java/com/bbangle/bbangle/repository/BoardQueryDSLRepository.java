package com.bbangle.bbangle.repository;

import java.util.List;

import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;

public interface BoardQueryDSLRepository {
    List<BoardResponseDto> getBoardResponseDto();
    BoardDetailResponseDto getBoardDetailResponseDto(Long boardId);
}

