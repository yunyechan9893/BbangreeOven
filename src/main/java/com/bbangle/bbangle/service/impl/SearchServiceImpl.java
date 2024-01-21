package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.repository.RedisRepository;
import com.bbangle.bbangle.repository.SearchRepository;
import com.bbangle.bbangle.service.SearchService;
import com.bbangle.bbangle.util.KomoranUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    @Autowired
    SearchRepository searchRepository;
    @Autowired
    RedisRepository redisRepository;

    @Override
    public Slice<BoardResponseDto> getBoardIdes(String title) {
        List<String> keys = getTokenizer(title);
        System.out.println(keys);


        // 검증 완료
        List<Long> boardIndexs = keys.stream()
                .map(key -> redisRepository.get(key))
                .filter(list -> list != null)  // Filter out null lists
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());


        boardIndexs.stream().forEach(object ->
                System.out.println(object)
        );

        int pageNumber = 0;  // 첫 번째 페이지
        int pageSize = 10;  // 페이지당 10개 아이템

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return searchRepository.getSearchBoardResult(boardIndexs, pageable);
    }

    private List<String> getTokenizer(String title){
        var komoran = KomoranUtil.getInstance();
        KomoranResult analyzeResultList = komoran.analyze(title);
        return analyzeResultList.getMorphesByTags("NNG", "NNP", "NNB", "NP", "NR", "NA");
    }
}