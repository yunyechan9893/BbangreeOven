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

    public static SearchBoardDto getEmpty(int pageNumber, int limitItemCount){
        return SearchBoardDto.builder().content(List.of())
                .itemAllCount(0)
                .pageNumber(pageNumber)
                .limitItemCount(limitItemCount)
                .currentItemCount(0)
                .existNextPage(false)
                .build();
    }
}
