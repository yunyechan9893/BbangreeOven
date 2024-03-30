package com.bbangle.bbangle.repository.impl;


import com.bbangle.bbangle.notice.dto.NoticeDetailResponseDto;
import com.bbangle.bbangle.notice.repository.NoticeRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NoticeRepositoryTest {

    @Autowired
    NoticeRepositoryImpl noticeRepository;

    @Test
    @DisplayName("공지사항 상세페이지를 조회한다")
    public void getNoticeList() throws Exception{
        //given
        Long noticeId = 1L;

        //when
        NoticeDetailResponseDto noticeDetail = noticeRepository.getNoticeDetail(noticeId);

        //then
        assertThat(noticeDetail.title()).isEqualTo("Privacy Policy Guide");
        assertThat(noticeDetail.content()).isEqualTo("Our company respects your privacy and is committed to protecting it.");
    }
}
