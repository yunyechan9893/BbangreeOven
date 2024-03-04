package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.NoticeDetailResponseDto;
import com.bbangle.bbangle.dto.NoticePagingResponseDto;
import com.bbangle.bbangle.dto.NoticeSaveRequestDto;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    NoticePagingResponseDto getNoticePagingList(Pageable pageable);

    NoticeDetailResponseDto getNoticeDetail(Long id);

    void saveNotice(NoticeSaveRequestDto noticeSaveRequestDto);

}
