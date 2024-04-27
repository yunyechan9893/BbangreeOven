package com.bbangle.bbangle.notice.dto;

import com.bbangle.bbangle.notice.domain.Notice;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@Builder
public record NotificationDetailResponseDto(
    Long id,
    String title,
    String content,
    String createdAt
) {

    public static NotificationDetailResponseDto from(Notice notice) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = notice.getCreatedAt().format(formatter);
        return new NotificationDetailResponseDto(
            notice.getId(),
            notice.getTitle(),
            notice.getContent(),
            formattedDateTime);
    }
}
