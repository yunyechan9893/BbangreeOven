package com.bbangle.bbangle.notice.service;

import com.bbangle.bbangle.notice.dto.NoticeDetailResponseDto;
import com.bbangle.bbangle.notice.dto.NoticePagingResponseDto;
import com.bbangle.bbangle.notice.dto.NoticeSaveRequestDto;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    NoticePagingResponseDto getNoticePagingList(Pageable pageable);

    NoticeDetailResponseDto getNoticeDetail(Long id);

    void saveNotice(NoticeSaveRequestDto noticeSaveRequestDto);

}
