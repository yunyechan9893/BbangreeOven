package com.bbangle.bbangle.board.dto;

import com.bbangle.bbangle.store.dto.StoreDto;
import lombok.Builder;

@Builder
public record BoardDetailResponseDto(
    StoreDto store,
    BoardDetailDto board
) {

}


