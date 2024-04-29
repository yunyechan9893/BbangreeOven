package com.bbangle.bbangle.page;

import com.bbangle.bbangle.notification.dto.NotificationResponse;
import java.util.List;

public class NotificationCustomPage<T> extends CustomPage<T> {
    public NotificationCustomPage(T content, Long requestCursor, Boolean hasNext) {
        super(content, requestCursor, hasNext);
    }

    public static NotificationCustomPage<List<NotificationResponse>> from(
        List<NotificationResponse> notificationResponseList,
        Long requestCursor,
        Boolean hasNext
    ) {
        return new NotificationCustomPage<>(notificationResponseList, requestCursor, hasNext);
    }
}
