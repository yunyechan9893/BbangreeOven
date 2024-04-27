package com.bbangle.bbangle.notice.service;

import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTIFICATION_NOT_FOUND;

import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.notice.domain.Notice;
import com.bbangle.bbangle.notice.dto.NotificationDetailResponseDto;
import com.bbangle.bbangle.notice.dto.NotificationResponse;
import com.bbangle.bbangle.notice.dto.NotificationUploadRequest;
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

    public NotificationDetailResponseDto getNoticeDetail(Long id) {
        Notice notification = notificationRepository.findById(id).orElseThrow(
            () -> new BbangleException(NOTIFICATION_NOT_FOUND)
        );
        return NotificationDetailResponseDto.from(notification);
    }

    @Transactional
    public void upload(NotificationUploadRequest notificationUploadRequest) {
        Notice newNotice = Notice.builder()
            .title(notificationUploadRequest.title())
            .content(notificationUploadRequest.content())
            .build();

        notificationRepository.save(newNotice);
    }

}
