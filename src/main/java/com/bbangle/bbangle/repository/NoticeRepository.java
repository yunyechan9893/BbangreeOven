package com.bbangle.bbangle.repository;


import com.bbangle.bbangle.model.Notice;
import com.bbangle.bbangle.repository.queryDsl.NoticeQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeQueryDSLRepository {
}
