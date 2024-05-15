package com.bbangle.bbangle.board.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record BoardResponse(
    Long boardId,
    String boardProfile,
    String boardTitle,
    Integer boardPrice,
    Boolean monday,
    Boolean tuesday,
    Boolean wednesday,
    Boolean thursday,
    Boolean friday,
    Boolean saturday,
    Boolean sunday,
    String purchaseUrl,
    Boolean isWished,
    Boolean status,
    List<BoardDetailDto> boardDetails
) {

}
