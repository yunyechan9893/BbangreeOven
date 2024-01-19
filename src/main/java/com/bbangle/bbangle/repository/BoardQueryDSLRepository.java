package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardQueryDSLRepository {

    Slice<BoardResponseDto> getBoardResponseDto(String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                                Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                                String category, Integer minPrice, Integer maxPrice, Pageable pageable);
    BoardDetailResponseDto getBoardDetailResponseDto(Long boardId);
}

