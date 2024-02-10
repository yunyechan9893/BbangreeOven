package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.dto.NoticeResponseDto;
import com.bbangle.bbangle.model.Notice;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NoticeQueryDSLRepository {
    Page<NoticeResponseDto> getNoticeList(Pageable pageable);
}
