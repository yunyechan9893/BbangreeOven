package com.bbangle.bbangle.repository;


import com.bbangle.bbangle.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    //클라이언트에 저장된 멤버 id를 가져와 리프레시 토큰을 확인하고 액세스 토큰을 발급한다
    Optional<RefreshToken> findByMemberId(Long memberId);
}
