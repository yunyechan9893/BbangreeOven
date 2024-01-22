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

    private final String BOARD="board";
    private final String STORE="store";

    @Override
    public Slice<BoardResponseDto> getBoardIdes(String title) {
        List<String> keys = getAllTokenizer(title);

        // 검증 완료
        List<Long> boardIndexs = keys.stream()
                .map(key -> redisRepository.get(BOARD, key))
                .filter(list -> list != null)  // Filter out null lists
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        List<Long> storeIndexs = keys.stream()
                .map(key -> redisRepository.get(STORE, key))
                .filter(list -> list != null)  // Filter out null lists
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        int pageNumber = 0;  // 첫 번째 페이지
        int pageSize = 10;  // 페이지당 10개 아이템

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        var searchBoardResult = searchRepository.getSearchResult(boardIndexs, pageable);


        return searchBoardResult;
    }

    private KomoranResult getTokenizer(String title) {
        var komoran = KomoranUtil.getInstance();
        return komoran.analyze(title);
    }

    private List<String> getAllTokenizer(String title) {
        System.out.println(getTokenizer(title).getTokenList());
        return getTokenizer(title).getTokenList().stream().map(token-> {
            System.out.println(token.getMorph());
            return token.getMorph();
        }).toList();
    }
}