package com.bbangle.bbangle.store.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PopularBoardResponse {

    private Long boardId;
    private String boardProfile;
    private String boardTitle;
    private Integer boardPrice;
    private Boolean isWished;
    private Boolean isBundled;

    public void setWishlist(boolean isWished) {
        this.isWished = isWished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PopularBoardResponse that = (PopularBoardResponse) o;
        return Objects.equals(boardId, that.boardId) &&
            Objects.equals(boardProfile, that.boardProfile) &&
            Objects.equals(boardTitle, that.boardTitle) &&
            Objects.equals(boardPrice, that.boardPrice) &&
            Objects.equals(isWished, that.isWished) &&
            Objects.equals(isBundled, that.isBundled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, boardProfile, boardTitle, boardPrice, isWished, isBundled);
    }
}