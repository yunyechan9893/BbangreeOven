package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.NoticePagingResponseDto;
import com.bbangle.bbangle.service.impl.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeServiceImpl noticeServiceImpl;

    @GetMapping
    public NoticePagingResponseDto getNotices(Pageable pageable){
        return noticeServiceImpl.getNoticePagingList(pageable);
    }
}
