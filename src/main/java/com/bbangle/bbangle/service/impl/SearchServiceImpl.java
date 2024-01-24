package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.SearchResponseDto;
import com.bbangle.bbangle.dto.StoreResponseDto;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.repository.RedisRepository;
import com.bbangle.bbangle.repository.SearchRepository;
import com.bbangle.bbangle.repository.StoreRepository;
import com.bbangle.bbangle.service.SearchService;
import com.bbangle.bbangle.util.KomoranUtil;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    @Autowired
    SearchRepository searchRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    RedisRepository redisRepository;


    private final String BOARD="board";
    private final String STORE="store";

    @Override
    public SearchResponseDto getSearchResult(String keyword) {
        // 검색어 저장
        System.out.println(searchRepository.saveSearchKeyword(0L, keyword));

        // 검색어 토큰화
        List<String> keys = getAllTokenizer(keyword);

        // 토큰화된 검색어를 통해 게시판 아이디 가져오기
        List<Long> boardIndexs = keys.stream()
                .map(key -> redisRepository.get(BOARD, key))
                .filter(list -> list != null)  // Filter out null lists
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        // 토큰화된 검색어를 통해 스토어 아이디 가져오기
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


        //스토어 및 보드 검색 결과 가져오기
        var searchBoardResult = searchRepository.getSearchResult(boardIndexs, pageable);
        var searchStoreResult = getList(storeIndexs, pageable);

        return new SearchResponseDto(searchBoardResult, searchStoreResult);
    }

    public Slice<StoreResponseDto> getList(List<Long> ides, Pageable pageable) {
        Slice<Store> sliceBy = storeRepository.findByIdIn(ides ,pageable);

        List<StoreResponseDto> dtoList = sliceBy.getContent()
                .stream()
                .map(StoreResponseDto::fromWithoutLogin)
                .collect(Collectors.toList());

        return new SliceImpl<>(dtoList, pageable, sliceBy.hasNext());
    }

    private KomoranResult getTokenizer(String keyword) {
        var komoran = KomoranUtil.getInstance();
        return komoran.analyze(keyword);
    }

    private List<String> getAllTokenizer(String keyword) {
        System.out.println(getTokenizer(keyword).getTokenList());
        return getTokenizer(keyword).getTokenList().stream().map(token-> {
            System.out.println(token.getMorph());
            return token.getMorph();
        }).toList();
    }
}