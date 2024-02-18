package com.bbangle.bbangle.dto;

import java.time.LocalDateTime;
import com.bbangle.bbangle.model.Notice;

public record NotificationResponse (
    String title,
    String content,
    LocalDateTime createdAt
){

    public static NotificationResponse from(Notice notice){
        return new NotificationResponse(
            notice.getTitle(),
            notice.getContent(),
            notice.getCreatedAt());
    }
}
