package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;

import java.util.List;

public interface BoardService {

    List<BoardResponseDto> getBoardList(String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                        Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                        String category);
    BoardDetailResponseDto getBoardDetailResponse(Long boardId);
}