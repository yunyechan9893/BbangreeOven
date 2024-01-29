package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.KeywordDto;
import com.bbangle.bbangle.dto.SearchResponseDto;
import com.bbangle.bbangle.dto.StoreResponseDto;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.model.RedisEnum;
import com.bbangle.bbangle.model.Search;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final int ONE_HOUR = 3600000;
    private final String BEST_KEYWORD_KEY = "keyword";

    @Autowired
    SearchRepository searchRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    RedisRepository redisRepository;

    @Override
    public SearchResponseDto getSearchResult(String keyword) {
        // 검색어 저장
        Long memberId = 1L;

        searchRepository.save(
                Search.builder()
                        .member(Member.builder()
                                .id(memberId)
                                .build())
                        .keyword(keyword)
                        .createdAt(LocalDateTime.now())
                        .build());


        // 검색어 토큰화
        List<String> keys = getAllTokenizer(keyword);

        // 토큰화된 검색어를 통해 게시판 아이디 가져오기
        List<Long> boardIndexs = keys.stream()
                .map(key -> redisRepository.get(RedisEnum.BOARD.label(), key))
                .filter(list -> list != null)  // Filter out null lists
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        // 토큰화된 검색어를 통해 스토어 아이디 가져오기
        List<Long> storeIndexs = keys.stream()
                .map(key -> redisRepository.get(RedisEnum.STORE.label(), key))
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

    @Override
    public List<KeywordDto> getRecencyKeyword(Long memberId) {
        return searchRepository.getRecencyKeyword(
                Member.builder()
                        .id(memberId)
                        .build()
        );
    }

    @Override
    public Boolean deleteRecencyKeyword(Long keywordId) {
        Long memberId = 1L;

        try {
            // UPDATE search SET search.isDeleted WHERE id=keywordId AND member = member;
            searchRepository.markAsDeleted(keywordId,
                    Member.builder().
                            id(memberId).
                            build());
            return true;
        } catch (Exception e){
            e.getMessage();
            return false;
        }

    }

    @Override
    @Scheduled(fixedRate = ONE_HOUR)
    public void updateRedisAtBestKeyword() {
        String[] bestKeyword = searchRepository.getBestKeyword();

        redisRepository.delete(RedisEnum.BEST_KEYWORD.label(),
                BEST_KEYWORD_KEY);

        redisRepository.set(
                RedisEnum.BEST_KEYWORD.label(),
                BEST_KEYWORD_KEY,
                bestKeyword);
    }

    @Override
    public List<String> getBestKeyword() {
        return redisRepository.getStringList(
                RedisEnum.BEST_KEYWORD.label(),
                BEST_KEYWORD_KEY
        );
    }
}