package com.bbangle.bbangle.notice.service;

import com.bbangle.bbangle.notice.dto.NoticeUploadRequest;
import com.bbangle.bbangle.notice.dto.NotificationResponse;
import com.bbangle.bbangle.notice.domain.Notice;
import com.bbangle.bbangle.notice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getList(Pageable pageable) {
        return notificationRepository.findAll(pageable)
            .map(NotificationResponse::from);
    }

    @Transactional
    public void upload(NoticeUploadRequest noticeUploadRequest) {
        Notice newNotice = Notice.builder()
            .title(noticeUploadRequest.title())
            .content(noticeUploadRequest.content())
            .build();

        notificationRepository.save(newNotice);
    }

}
