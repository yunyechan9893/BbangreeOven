package com.bbangle.bbangle.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.util.List;
import java.util.Objects;

@Builder
public final class BoardDto {

    private final Long boardId;
    private final String thumbnail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<BoardImgDto> images;
    private final String title;
    private final int price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final BoardAvailableDayDto orderAvailableDays;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String purchaseUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isWished;
    private final Boolean isBundled;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String detail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<String> tags;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<ProductDto> products;

    public BoardDto(
        Long boardId,
        String thumbnail,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<BoardImgDto> images,
        String title,
        int price,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        BoardAvailableDayDto orderAvailableDays,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String purchaseUrl,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean isWished,
        Boolean isBundled,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String detail,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<String> tags,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<ProductDto> products
    ) {
        this.boardId = boardId;
        this.thumbnail = thumbnail;
        this.images = images;
        this.title = title;
        this.price = price;
        this.orderAvailableDays = orderAvailableDays;
        this.purchaseUrl = purchaseUrl;
        this.isWished = isWished;
        this.isBundled = isBundled;
        this.detail = detail;
        this.tags = tags;
        this.products = products;
    }

    public void updateLike(boolean status){
        this.isWished = status;
    }

    public Long boardId() {
        return boardId;
    }

    public String thumbnail() {
        return thumbnail;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<BoardImgDto> images() {
        return images;
    }

    public String title() {
        return title;
    }

    public int price() {
        return price;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public BoardAvailableDayDto orderAvailableDays() {
        return orderAvailableDays;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String purchaseUrl() {
        return purchaseUrl;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean isWished() {
        return isWished;
    }

    public Boolean isBundled() {
        return isBundled;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String detail() {
        return detail;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<String> tags() {
        return tags;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<ProductDto> products() {
        return products;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (BoardDto) obj;
        return Objects.equals(this.boardId, that.boardId) &&
            Objects.equals(this.thumbnail, that.thumbnail) &&
            Objects.equals(this.images, that.images) &&
            Objects.equals(this.title, that.title) &&
            this.price == that.price &&
            Objects.equals(this.orderAvailableDays, that.orderAvailableDays) &&
            Objects.equals(this.purchaseUrl, that.purchaseUrl) &&
            Objects.equals(this.isWished, that.isWished) &&
            Objects.equals(this.isBundled, that.isBundled) &&
            Objects.equals(this.detail, that.detail) &&
            Objects.equals(this.tags, that.tags) &&
            Objects.equals(this.products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, thumbnail, images, title, price, orderAvailableDays,
            purchaseUrl, isWished, isBundled, detail, tags, products);
    }

    @Override
    public String toString() {
        return "BoardDto[" +
            "boardId=" + boardId + ", " +
            "thumbnail=" + thumbnail + ", " +
            "images=" + images + ", " +
            "title=" + title + ", " +
            "price=" + price + ", " +
            "orderAvailableDays=" + orderAvailableDays + ", " +
            "purchaseUrl=" + purchaseUrl + ", " +
            "isWished=" + isWished + ", " +
            "isBundled=" + isBundled + ", " +
            "detail=" + detail + ", " +
            "tags=" + tags + ", " +
            "products=" + products + ']';
    }
}
