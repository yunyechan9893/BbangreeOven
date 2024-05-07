package com.bbangle.bbangle.preference.repository;

import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.preference.domain.QMemberPreference;
import com.bbangle.bbangle.preference.domain.QPreference;
import com.bbangle.bbangle.preference.dto.MemberPreferenceResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PreferenceRepositoryImpl implements PreferenceQueryDSLRepository{

    private final JPAQueryFactory queryFactory;
    private static final QPreference preference = QPreference.preference;
    private static final QMemberPreference memberPreference = QMemberPreference.memberPreference;


    @Override
    public MemberPreferenceResponse getMemberPreference(Member member) {
        return queryFactory.select(Projections.constructor(
            MemberPreferenceResponse.class,
            preference.preferenceType
        )).from(preference)
            .join(preference, memberPreference.preference)
            .where(memberPreference.member.eq(member))
            .fetchOne();
    }

}
