package com.bbangle.bbangle.search.dto;

import com.bbangle.bbangle.board.dto.BoardResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record SearchBoardDto(
    List<BoardResponseDto> content,
    int itemAllCount,
    int limitItemCount,
    int currentItemCount,
    int pageNumber,
    boolean existNextPage
) {

}
