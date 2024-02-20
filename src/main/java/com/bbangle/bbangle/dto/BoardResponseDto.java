package com.bbangle.bbangle.dto;

import java.util.HashMap;
import java.util.List;
import com.bbangle.bbangle.model.Board;
import lombok.Builder;

@Builder
public record BoardResponseDto(
    Long boardId,
    Long storeId,
    String storeName,
    String thumbnail,
    String title,
    int price,
    Boolean isWished,
    Boolean isBundled,
    List<String> tags
) {

    public static BoardResponseDto from(Board board, List<String> tags){
        boolean isBundled = board.getProductList().size() > 1;

        return BoardResponseDto.builder()
            .boardId(board.getId())
            .storeId(board.getStore().getId())
            .storeName(board.getStore().getName())
            .thumbnail(board.getProfile())
            .title(board.getTitle())
            .price(board.getPrice())
            .isWished(false)
            .isBundled(isBundled)
            .tags(tags)
            .build();
    }
}
