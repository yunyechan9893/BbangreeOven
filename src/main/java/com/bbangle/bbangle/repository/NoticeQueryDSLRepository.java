package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.dto.NoticeDetailResponseDto;
import com.bbangle.bbangle.dto.NoticeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeQueryDSLRepository {

    Page<NoticeResponseDto> getNoticeList(Pageable pageable);

    NoticeDetailResponseDto getNoticeDetail(Long id);

}
