package com.bbangle.bbangle.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record SearchBoardDto(
        List<BoardResponseDto> content,
        int itemCount,
        int pageNumber,
        int itemSize,
        boolean existNextPage
){
}
