package com.bbangle.bbangle.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDateTime;

public record NoticeDetailResponseDto(Long id,
                                       String title,
                                       String content,
                                       String createdAt) {
    @QueryProjection
    public NoticeDetailResponseDto(Long id, String title, String content, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
