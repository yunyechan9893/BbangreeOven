package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.model.RedisEnum;
import com.bbangle.bbangle.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SpringBootTest
public class RedisRepositoryTest {
    @Autowired
    RedisRepository redisRepository;

    final String boardNameSpace = RedisEnum.BOARD.name();
    final String testKeyword = "비건";
    final String[] testBoardId = {"1","2","3","4","5"};

    @AfterEach
    void after(){
        redisRepository.deleteAll();
    }

    @Test
    @DisplayName("Redis에 보드값을 저장하면 정상적으로 값을 가져올 수 있어야한다")
    void saveBoardValue(){
        redisRepository.set(boardNameSpace, testKeyword, testBoardId);
        var result = redisRepository.get(boardNameSpace, testKeyword);
        Assertions.assertEquals(5, result.size(), "결과값 개수가 모자랍니다");
        
        AtomicLong assertNumber = new AtomicLong(1L);
        result.forEach(boardId ->{
            Assertions.assertEquals(boardId, assertNumber.get(), String.format("%s 값이 다릅니다",boardId));
            assertNumber.getAndIncrement();
        });
    }
}
