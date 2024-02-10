package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.dto.NoticeResponseDto;
import com.bbangle.bbangle.dto.QNoticeResponseDto;
import com.bbangle.bbangle.repository.NoticeQueryDSLRepository;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.bbangle.bbangle.model.QNotice.notice;

@Repository
@RequiredArgsConstructor
public class NoticeQueryDSLRepositoryImpl implements NoticeQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NoticeResponseDto> getNoticeList(Pageable pageable) {
        //날짜 변환
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , notice.createdAt
                , ConstantImpl.create("%Y-%m-%d %H:%i"));

        List<NoticeResponseDto> notices = queryFactory
                .select(new QNoticeResponseDto(
                        notice.id,
                        notice.title,
                        formattedDate
                ))
                .from(notice)
                .orderBy(notice.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(notices, pageable, queryFactory
                .select(new QNoticeResponseDto(
                        notice.id,
                        notice.title,
                        formattedDate
                ))
                .from(notice)
                .orderBy(notice.createdAt.desc())
                .fetch().size());
    }
}
