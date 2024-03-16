package com.bbangle.bbangle.notice.controller;

import com.bbangle.bbangle.notice.dto.NoticeUploadRequest;
import com.bbangle.bbangle.notice.dto.NotificationResponse;
import com.bbangle.bbangle.notice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getList(
        @PageableDefault
        Pageable pageable
    ) {
        return ResponseEntity.ok(notificationService.getList(pageable));
    }

    @PostMapping
    public ResponseEntity<Void> upload(
        @RequestBody
        NoticeUploadRequest noticeUploadRequest
    ) {
        notificationService.upload(noticeUploadRequest);
        return ResponseEntity.ok()
            .build();
    }

}
