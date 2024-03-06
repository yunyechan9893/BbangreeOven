package com.bbangle.bbangle.repository;


import com.bbangle.bbangle.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);

}
