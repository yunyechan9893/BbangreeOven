package com.bbangle.bbangle.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NoticeDetailResponseDto(Long id,
                                       String title,
                                       String content,
                                       LocalDateTime createdAt) {
}
