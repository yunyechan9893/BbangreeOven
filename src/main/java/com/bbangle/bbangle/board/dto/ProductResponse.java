package com.bbangle.bbangle.board.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ProductResponse(
    Boolean boardIsBundled,
    List<BoardDetailProductDto> products
) {

}
