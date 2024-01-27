package com.bbangle.bbangle.service;

import com.bbangle.bbangle.model.RedisEnum;
import com.bbangle.bbangle.repository.InitRepository;
import com.bbangle.bbangle.repository.RedisRepository;
import com.bbangle.bbangle.util.KomoranUtil;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SearchServiceTest {
    private final String BEST_KEYWORD_KEY = "keyword";

    @Autowired
    InitRepository initRepository;

    @Autowired
    SearchService searchService;

    @Autowired
    RedisRepository redisRepository;

    @Test
    public void getAllBoardTitleTest(){
        var result = initRepository.getAllBoardTitle();
        System.out.println(result);
    }
    @Test
    public void getBoardIdesTest(){
        searchService.getSearchResult("비건 베이커리");
    }

    @Test
    public List<String> KomoranUtilSingletonTest(String title) {
        var komoran = KomoranUtil.getInstance();
        KomoranResult analyzeResultList = komoran.analyze(title);

        List<Token> tokenList = analyzeResultList.getTokenList();

        return analyzeResultList.getMorphesByTags("NNG", "NNP", "NNB", "SL");
    }

    @Test
    public void getBestKeyword(){
        searchService.updateRedisAtBestKeyword();

        var result = redisRepository.getStringList(
                RedisEnum.BEST_KEYWORD.label(),
                BEST_KEYWORD_KEY
        );

        Assertions.assertEquals(result.size(), 7);

        searchService.updateRedisAtBestKeyword();

        result = redisRepository.getStringList(
                RedisEnum.BEST_KEYWORD.label(),
                BEST_KEYWORD_KEY
        );

        System.out.println(result);
        Assertions.assertEquals(result.size(), 7);

    }


}
