package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.member.domain.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
