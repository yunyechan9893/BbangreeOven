package com.bbangle.bbangle.notification.repository;

import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTIFICATION_NOT_FOUND;

import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.notification.domain.Notice;
import com.bbangle.bbangle.notification.domain.QNotice;
import com.bbangle.bbangle.notification.dto.NotificationResponse;
import com.bbangle.bbangle.page.NotificationCustomPage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationQueryDSLRepository{

    private static final Long PAGE_SIZE = 20L;
    private final JPAQueryFactory queryFactory;
    private final QNotice notice = QNotice.notice;
    @Override
    public NotificationCustomPage<List<NotificationResponse>> findNextCursorPage(Long cursorId) {
        BooleanBuilder cursorCondition = getCursorCondition(cursorId);
        List<Notice> notifications = queryFactory.selectFrom(notice)
            .where(cursorCondition)
            .limit(PAGE_SIZE + 1)
            .orderBy(notice.createdAt.desc(), notice.id.desc())
            .fetch();
        List<NotificationResponse> responseDtos = notifications.stream()
            .map(Notice::makeNotificationResponse)
            .collect(Collectors.toList());

        boolean hasNext = checkingHasNext(responseDtos);
        Long requestCursor = responseDtos.get(responseDtos.size()-1).id();

        if (hasNext) {
            responseDtos.remove(responseDtos.get(responseDtos.size()-1));
        }

        return NotificationCustomPage.from(responseDtos, requestCursor, hasNext);
    }

    private static boolean checkingHasNext(List<NotificationResponse> responseDtos) {
        return responseDtos.size() >= PAGE_SIZE + 1;
    }

    private BooleanBuilder getCursorCondition(Long cursorId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Objects.isNull(cursorId)) {
            return booleanBuilder;
        }
        Long startId = checkingNotificationExistence(cursorId);

        booleanBuilder.and(notice.id.loe(startId));
        return booleanBuilder;
    }

    private Long checkingNotificationExistence(Long cursorId) {
        Long checkingId = queryFactory.select(notice.id)
            .from(notice)
            .where(notice.id.eq(cursorId))
            .fetchOne();

        if (Objects.isNull(checkingId)) {
            throw new BbangleException(NOTIFICATION_NOT_FOUND);
        }

        return cursorId+1;
    }
}
