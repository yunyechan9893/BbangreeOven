package com.bbangle.bbangle.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.bbangle.bbangle.dto.QNoticeDetailResponseDto is a Querydsl Projection type for NoticeDetailResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QNoticeDetailResponseDto extends ConstructorExpression<NoticeDetailResponseDto> {

    private static final long serialVersionUID = 21787139L;

    public QNoticeDetailResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<String> createdAt) {
        super(NoticeDetailResponseDto.class, new Class<?>[]{long.class, String.class, String.class, String.class}, id, title, content, createdAt);
    }

}

