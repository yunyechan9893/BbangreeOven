package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.NoticePagingResponseDto;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NoticeService {
    NoticePagingResponseDto getNoticePagingList(Pageable pageable);
}
