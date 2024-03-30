package com.bbangle.bbangle.notice.dto;

import com.querydsl.core.annotations.QueryProjection;

public record NoticeDetailResponseDto(
    Long id,
    String title,
    String content,
    String createdAt
) {

    @QueryProjection
    public NoticeDetailResponseDto(Long id, String title, String content, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

}
