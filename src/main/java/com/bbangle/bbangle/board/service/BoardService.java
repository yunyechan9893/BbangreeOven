package com.bbangle.bbangle.board.service;

import com.bbangle.bbangle.board.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.board.dto.BoardResponseDto;
import com.bbangle.bbangle.page.CustomPage;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface BoardService {

    CustomPage<List<BoardResponseDto>> getBoardList(
        String sort,
        Boolean glutenFreeTag,
        Boolean highProteinTag,
        Boolean sugarFreeTag,
        Boolean veganTag,
        Boolean ketogenicTag,
        String category,
        Integer minPrice,
        Integer maxPrice,
        Boolean orderAvailableToday,
        Long cursorId
    );

    @Transactional(readOnly = true)
    BoardDetailResponseDto getBoardDetailResponse(Long memberId, Long boardId);
}
