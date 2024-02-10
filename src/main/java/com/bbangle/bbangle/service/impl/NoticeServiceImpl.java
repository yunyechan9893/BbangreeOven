package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.NoticePagingResponseDto;
import com.bbangle.bbangle.repository.impl.NoticeQueryDSLRepositoryImpl;
import com.bbangle.bbangle.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeQueryDSLRepositoryImpl noticeQueryDSLRepositoryImpl;

    @Override
    public NoticePagingResponseDto getNoticePagingList(Pageable pageable) {
       return NoticePagingResponseDto.of(noticeQueryDSLRepositoryImpl.getNoticeList(pageable));
    }
}
