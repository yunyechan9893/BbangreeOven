package com.bbangle.bbangle.member.repository;

import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fromDate = now.minusDays(days);

        return queryFactory.select(member.id.count())
                .from(member)
                .where(member.createdAt.between(fromDate, now))
                .fetchOne();
    }

}
