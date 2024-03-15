package com.bbangle.bbangle.store.dto;

import com.bbangle.bbangle.board.dto.StoreBestBoardDto;
import java.util.List;
import lombok.Builder;

@Builder
public record StoreDetailResponseDto(
    StoreDto store,

    List<StoreBestBoardDto> bestProducts
) {

}
