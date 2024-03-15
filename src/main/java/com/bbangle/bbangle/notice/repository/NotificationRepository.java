package com.bbangle.bbangle.notice.repository;

import com.bbangle.bbangle.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notice, Long> {

}
