package com.bbangle.bbangle.member.repository;

import com.bbangle.bbangle.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
}
