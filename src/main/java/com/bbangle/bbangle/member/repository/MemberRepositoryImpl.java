package com.bbangle.bbangle.member.repository;

import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.domain.QMember;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_MEMBER;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberQueryDSLRepository{

    private static final QMember member = QMember.member;

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findMemberById(Long memberId) {
        return Optional.ofNullable(queryFactory.selectFrom(member)
            .where(member.id.eq(memberId))
            .fetchOne())
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));
    }

    @Override
    public Long countNewMember(int days) {
        LocalDate now = LocalDate.now();
        LocalDate fromDate = now.minusDays(days);

        DateTemplate<LocalDate> createdAt = Expressions.dateTemplate(LocalDate.class, "DATE({0})", member.createdAt);

        return queryFactory.select(member.id.count())
                .from(member)
                .where(createdAt.between(fromDate, now))
                .fetchOne();
    }

}
