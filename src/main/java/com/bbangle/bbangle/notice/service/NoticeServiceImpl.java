package com.bbangle.bbangle.notice.service;

import com.bbangle.bbangle.notice.dto.NoticeDetailResponseDto;
import com.bbangle.bbangle.notice.dto.NoticePagingResponseDto;
import com.bbangle.bbangle.notice.dto.NoticeSaveRequestDto;
import com.bbangle.bbangle.notice.domain.Notice;
import com.bbangle.bbangle.notice.repository.NoticeRepository;
import com.bbangle.bbangle.notice.repository.NoticeRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepositoryImpl noticeQueryDSLRepositoryImpl;
    private final NoticeRepository noticeRepository;

    @Override
    public NoticePagingResponseDto getNoticePagingList(Pageable pageable) {
        return NoticePagingResponseDto.of(noticeQueryDSLRepositoryImpl.getNoticeList(pageable));
    }

    @Override
    public NoticeDetailResponseDto getNoticeDetail(Long id) {
        return noticeQueryDSLRepositoryImpl.getNoticeDetail(id);
    }

    @Override
    @Transactional
    public void saveNotice(NoticeSaveRequestDto noticeSaveRequestDto) {
        Notice notice = Notice.builder()
            .title(noticeSaveRequestDto.title())
            .content(noticeSaveRequestDto.content())
            .build();
        noticeRepository.save(notice);
    }

}
