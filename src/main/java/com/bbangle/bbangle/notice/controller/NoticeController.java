package com.bbangle.bbangle.notice.controller;

import com.bbangle.bbangle.notice.dto.NoticeDetailResponseDto;
import com.bbangle.bbangle.notice.dto.NoticePagingResponseDto;
import com.bbangle.bbangle.notice.dto.NoticeSaveRequestDto;
import com.bbangle.bbangle.notice.service.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeServiceImpl noticeServiceImpl;

    @GetMapping
    public NoticePagingResponseDto getNotices(Pageable pageable) {
        return noticeServiceImpl.getNoticePagingList(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDetailResponseDto> getNoticeDetail(
        @PathVariable
        Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(noticeServiceImpl.getNoticeDetail(id));
    }

    @PostMapping
    public ResponseEntity<Void> saveNotice(
        @RequestBody
        NoticeSaveRequestDto noticeSaveRequestDto
    ) {
        noticeServiceImpl.saveNotice(noticeSaveRequestDto);
        return ResponseEntity.ok()
            .build();
    }

}
