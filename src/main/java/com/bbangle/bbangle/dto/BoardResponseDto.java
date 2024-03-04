package com.bbangle.bbangle.dto;

import com.bbangle.bbangle.model.Board;
import java.util.List;
import java.util.Objects;
import lombok.Builder;

@Builder
public final class BoardResponseDto {

    private final Long boardId;
    private final Long storeId;
    private final String storeName;
    private final String thumbnail;
    private final String title;
    private final int price;
    private Boolean isWished;
    private final Boolean isBundled;
    private final List<String> tags;

    public BoardResponseDto(
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
        this.boardId = boardId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.thumbnail = thumbnail;
        this.title = title;
        this.price = price;
        this.isWished = isWished;
        this.isBundled = isBundled;
        this.tags = tags;
    }

    public static BoardResponseDto from(Board board, List<String> tags) {
        boolean isBundled = board.getProductList()
            .size() > 1;

        return BoardResponseDto.builder()
            .boardId(board.getId())
            .storeId(board.getStore()
                .getId())
            .storeName(board.getStore()
                .getName())
            .thumbnail(board.getProfile())
            .title(board.getTitle())
            .price(board.getPrice())
            .isWished(false)
            .isBundled(isBundled)
            .tags(tags)
            .build();
    }

    public void updateLike(boolean status){
        this.isWished = status;
    }

    public Long boardId() {
        return boardId;
    }

    public Long storeId() {
        return storeId;
    }

    public String storeName() {
        return storeName;
    }

    public String thumbnail() {
        return thumbnail;
    }

    public String title() {
        return title;
    }

    public int price() {
        return price;
    }

    public Boolean isWished() {
        return isWished;
    }

    public Boolean isBundled() {
        return isBundled;
    }

    public List<String> tags() {
        return tags;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (BoardResponseDto) obj;
        return Objects.equals(this.boardId, that.boardId) &&
            Objects.equals(this.storeId, that.storeId) &&
            Objects.equals(this.storeName, that.storeName) &&
            Objects.equals(this.thumbnail, that.thumbnail) &&
            Objects.equals(this.title, that.title) &&
            this.price == that.price &&
            Objects.equals(this.isWished, that.isWished) &&
            Objects.equals(this.isBundled, that.isBundled) &&
            Objects.equals(this.tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, storeId, storeName, thumbnail, title, price, isWished,
            isBundled, tags);
    }

    @Override
    public String toString() {
        return "BoardResponseDto[" +
            "boardId=" + boardId + ", " +
            "storeId=" + storeId + ", " +
            "storeName=" + storeName + ", " +
            "thumbnail=" + thumbnail + ", " +
            "title=" + title + ", " +
            "price=" + price + ", " +
            "isWished=" + isWished + ", " +
            "isBundled=" + isBundled + ", " +
            "tags=" + tags + ']';
    }


}
