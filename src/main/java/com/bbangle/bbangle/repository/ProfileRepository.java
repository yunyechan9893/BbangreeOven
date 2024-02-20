package com.bbangle.bbangle.repository;


import com.bbangle.bbangle.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);
}
