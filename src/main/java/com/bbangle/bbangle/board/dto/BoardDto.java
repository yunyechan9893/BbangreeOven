package com.bbangle.bbangle.board.dto;

import com.bbangle.bbangle.board.domain.QBoard;
import com.querydsl.core.Tuple;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardDto {

    private static final QBoard board = QBoard.board;

    private Long id;
    private String profile;
    private String title;
    private Integer price;
    private String purchaseUrl;
    private Boolean isLiked;
    private Boolean status;
    private Integer deliveryFee;
    private Integer freeShippingConditions;
    private List<String> boardImages;
    private List<BoardDetailDto> boardDetails;

    public static BoardDto of(Tuple boardTuple) {
        return BoardDto.builder()
            .id(boardTuple.get(board.id))
            .profile(boardTuple.get(board.profile))
            .title(boardTuple.get(board.title))
            .price(boardTuple.get(board.price))
            .purchaseUrl(boardTuple.get(board.purchaseUrl))
            .status(boardTuple.get(board.status))
            .deliveryFee(boardTuple.get(board.deliveryFee))
            .freeShippingConditions(boardTuple.get(board.freeShippingConditions))
            .build();
    }

    public void updateWished(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public void addBoardImageList(List<String> boardImageDtoList) {
        this.boardImages = boardImageDtoList;
    }

    public void addBoardDetailList(List<BoardDetailDto> boardDetailList) {
        this.boardDetails = boardDetailList;
    }

    public static Map<String, BoardDto> convertToMap(BoardDto boardDto) {
        return Map.of("board", boardDto);
    }
}
