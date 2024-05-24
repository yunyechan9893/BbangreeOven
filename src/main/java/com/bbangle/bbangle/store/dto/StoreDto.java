package com.bbangle.bbangle.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import java.util.Map;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreDto {

    private Long id;
    private String title;
    private String profile;
    private Boolean isLiked;

    @QueryProjection
    public StoreDto(
        Long id,
        String title,
        String profile) {
        this.id = id;
        this.title = title;
        this.profile = profile;
    }

    public void updateWished(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public static Map<String, StoreDto> convertToMap(StoreDto storeDto) {
        return Map.of("store", storeDto);
    }
}
