package com.bbangle.bbangle.notice.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record NoticeResponseDto(
    Long id,
    String title,
    String createdAt

) {

    @QueryProjection
    public NoticeResponseDto(Long id, String title, String createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

}
