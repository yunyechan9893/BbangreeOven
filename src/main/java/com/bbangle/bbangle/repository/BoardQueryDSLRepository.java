package com.bbangle.bbangle.repository;

import java.util.List;

import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;

public interface BoardQueryDSLRepository {
    List<BoardResponseDto> getBoardResponseDto(String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                               Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                               String category);
    BoardDetailResponseDto getBoardDetailResponseDto(Long boardId);
}

