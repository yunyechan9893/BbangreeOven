package com.bbangle.bbangle.notice.dto;

import com.bbangle.bbangle.notice.domain.Notice;
import java.time.LocalDateTime;

public record NotificationResponse(
    String title,
    String content,
    LocalDateTime createdAt
) {

    public static NotificationResponse from(Notice notice) {
        return new NotificationResponse(
            notice.getTitle(),
            notice.getContent(),
            notice.getCreatedAt());
    }

}
