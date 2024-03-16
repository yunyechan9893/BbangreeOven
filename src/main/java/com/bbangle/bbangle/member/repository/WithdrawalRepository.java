package com.bbangle.bbangle.member.repository;

import com.bbangle.bbangle.member.domain.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
