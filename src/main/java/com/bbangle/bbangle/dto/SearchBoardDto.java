package com.bbangle.bbangle.dto;

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
){
}