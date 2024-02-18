package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notice, Long> {
}
