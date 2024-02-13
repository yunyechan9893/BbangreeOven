package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.NoticeDetailResponseDto;
import com.bbangle.bbangle.dto.NoticePagingResponseDto;
import com.bbangle.bbangle.service.impl.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeServiceImpl noticeServiceImpl;

    @GetMapping
    public NoticePagingResponseDto getNotices(Pageable pageable){
        return noticeServiceImpl.getNoticePagingList(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDetailResponseDto> getNoticeDetail(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(noticeServiceImpl.getNoticeDetail(id));
    }
}
