package com.bbangle.bbangle.repository;


import com.bbangle.bbangle.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Member, Long> {

}
