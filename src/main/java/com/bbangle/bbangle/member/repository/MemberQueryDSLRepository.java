package com.bbangle.bbangle.member.repository;

import com.bbangle.bbangle.member.domain.Member;

public interface MemberQueryDSLRepository {

    Member findMemberById(Long memberId);

    Long countNewMember(int day);

}
