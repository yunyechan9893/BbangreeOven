package com.bbangle.bbangle.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardRepositoryImplTest {

    @Autowired
    BoardRepositoryImpl boardRepository;

    @Test
    public void getBoardResponseDtoTest(){
        Long boardId = Long.valueOf(1);
        boardRepository.getBoardDetailResponseDto(boardId);
    }
}
