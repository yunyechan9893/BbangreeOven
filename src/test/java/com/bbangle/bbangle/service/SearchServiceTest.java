package com.bbangle.bbangle.service;

import com.bbangle.bbangle.repository.InitRepository;
import com.bbangle.bbangle.repository.SearchRepository;
import com.bbangle.bbangle.util.KomoranUtil;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SearchServiceTest {

    @Autowired
    InitRepository initRepository;

    @Autowired
    SearchService searchService;

    @Test
    public void getAllBoardTitleTest(){
        var result = initRepository.getAllBoardTitle();
        System.out.println(result);
    }
    @Test
    public void getBoardIdesTest(){
        searchService.getBoardIdes("비건 베이커리");
    }

    @Test
    public List<String> KomoranUtilSingletonTest(String title) {
        var komoran = KomoranUtil.getInstance();
        KomoranResult analyzeResultList = komoran.analyze(title);

        List<Token> tokenList = analyzeResultList.getTokenList();

        return analyzeResultList.getMorphesByTags("NNG", "NNP", "NNB", "SL");
    }
}
