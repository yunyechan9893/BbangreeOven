package com.bbangle.bbangle.notice.repository;

import static com.bbangle.bbangle.notice.domain.QNotice.notice;

import com.bbangle.bbangle.notice.dto.NoticeDetailResponseDto;
import com.bbangle.bbangle.notice.dto.NoticeResponseDto;

import com.bbangle.bbangle.notice.dto.QNoticeDetailResponseDto;
import com.bbangle.bbangle.notice.dto.QNoticeResponseDto;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeQueryDSLRepository {

    private final JPAQueryFactory queryFactory;
    //날짜 변환
    private final StringTemplate FORMATTED_DATE = Expressions.stringTemplate(
            "DATE_FORMAT({0}, {1})"
                , notice.createdAt
                , ConstantImpl.create("%Y-%m-%d %H:%i"));

    @Override
    public Page<NoticeResponseDto> getNoticeList(Pageable pageable) {
        List<NoticeResponseDto> notices = queryFactory
                .select(new QNoticeResponseDto(
                        notice.id,
                        notice.title,
                        FORMATTED_DATE
                ))
                .from(notice)
                .orderBy(notice.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<NoticeResponseDto> totalQuery = queryFactory
                .select(new QNoticeResponseDto(
                        notice.id,
                        notice.title,
                        FORMATTED_DATE
                ))
                .from(notice)
                .orderBy(notice.createdAt.desc());

        return PageableExecutionUtils.getPage(notices, pageable, totalQuery::fetchCount);
    }

    @Override
    public NoticeDetailResponseDto getNoticeDetail(Long id) {
        return queryFactory
                .select(new QNoticeDetailResponseDto(
                        notice.id,
                        notice.title,
                        notice.content,
                        FORMATTED_DATE
                ))
                .from(notice)
                .where(notice.id.eq(id))
                .fetchOne();
    }
}
