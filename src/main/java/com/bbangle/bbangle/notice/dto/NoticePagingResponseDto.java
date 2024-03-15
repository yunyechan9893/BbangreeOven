package com.bbangle.bbangle.notice.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Getter
public class NoticePagingResponseDto {

    private static final long LAST_PAGE = -1L;

    private List<NoticeResponseDto> contents = new ArrayList<>();
    private long lastPage;
    private long nextPage;

    /**
     * 공지사항 페이지 네이션 응답 dto 생성
     *
     * @param noticePaging 공지사항 페이지네이션 정보를 담고 있는 객체
     * @return 공지사항 페이지 네이션 응답 dto
     */
    public static NoticePagingResponseDto of(Page<NoticeResponseDto> noticePaging) {
        //마지막 페이지를 요청했다면
        if (!noticePaging.hasNext()) {
            return NoticePagingResponseDto.newLastScroll(noticePaging.getContent(),
                noticePaging.getTotalPages() - 1);
        }
        return NoticePagingResponseDto.newPagingHasNext(noticePaging.getContent(),
            noticePaging.getTotalPages() - 1, //page는 0부터 시작
            noticePaging.getPageable()
                .getPageNumber() + 1);
    }

    //마지막 페이지인 경우
    private static NoticePagingResponseDto newLastScroll(
        List<NoticeResponseDto> noticePaging,
        long lastPage
    ) {
        return newPagingHasNext(noticePaging, lastPage, LAST_PAGE);
    }

    //마지막 페이지가 아닌 경우
    private static NoticePagingResponseDto newPagingHasNext(
        List<NoticeResponseDto> noticePaging,
        long lastPage,
        long nextPage
    ) {
        return new NoticePagingResponseDto(noticePaging, lastPage, nextPage);
    }

}
