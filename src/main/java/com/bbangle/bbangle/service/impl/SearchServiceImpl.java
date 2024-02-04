package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.KeywordDto;
import com.bbangle.bbangle.dto.SearchResponseDto;
import com.bbangle.bbangle.dto.StoreResponseDto;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.model.RedisEnum;
import com.bbangle.bbangle.model.Search;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.repository.InitRepository;
import com.bbangle.bbangle.repository.RedisRepository;
import com.bbangle.bbangle.repository.SearchRepository;
import com.bbangle.bbangle.repository.StoreRepository;
import com.bbangle.bbangle.service.SearchService;
import com.bbangle.bbangle.util.KomoranUtil;
import com.bbangle.bbangle.util.TrieUtil;
import jakarta.annotation.PostConstruct;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private static TrieUtil trie;
    private final String MIGRATION="migration";
    private final int ONE_HOUR = 3600000;
    private final String BEST_KEYWORD_KEY = "keyword";

    @Autowired
    SearchRepository searchRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    RedisRepository redisRepository;
    @Autowired
    InitRepository initRepository;

    @Override
    @PostConstruct
    public void loadData() {
        Map<String, List<Long>> resultBoardMap = null;
        Map<String, List<Long>> resultStoreMap = null;

        try{
            if (!redisRepository.get(RedisEnum.BOARD.label(), MIGRATION).isEmpty()){
                System.out.println("[완료] 이미 동기화가 되어 있습니다");

                return;
            }
        } catch (Exception e) {
            System.out.println("[에러] 레디스 서버 장애가 발생했습니다");
            return;
        }
        finally {
            // 코모란 싱글톤 활성화
            KomoranUtil.getInstance();
            trie = new TrieUtil();

            HashMap<Long, String> boardTitles = initRepository.getAllBoardTitle();
            resultBoardMap = getWord(boardTitles, RedisEnum.BOARD.label());

            HashMap<Long, String> storeTitles = initRepository.getAllStoreTitle();
            resultStoreMap = getWord(storeTitles, RedisEnum.BOARD.label());

            boardTitles.entrySet().forEach(entry ->{
                System.out.println(entry.getValue());
                trie.insert(entry.getValue());
            });

            resultBoardMap.entrySet().forEach(entry ->{
                System.out.println(entry.getValue());
                trie.insert(entry.getKey());
            });

            storeTitles.entrySet().forEach(entry ->{
                System.out.println(entry.getValue());
                trie.insert(entry.getValue());
            });

            resultStoreMap.entrySet().forEach(entry ->{
                System.out.println(entry.getValue());
                trie.insert(entry.getKey());
            });
        }

        System.out.println("[완료] 보드 동기화 중");
        uploadRedis(resultBoardMap, RedisEnum.BOARD.label());

        System.out.println("[완료] 스토어 동기화 중");
        uploadRedis(resultStoreMap, RedisEnum.STORE.label());

        System.out.println("[완료] 레디스에 동기화 완료");
    }

    private Map<String, List<Long>> getWord(HashMap<Long, String> targetTitles, String targetType){
        Map<String, List<Long>> resultMap = new HashMap<>();

        for (Map.Entry<Long, String> entry : targetTitles.entrySet()) {
            Long id = entry.getKey();
            String title = entry.getValue();
            List<String> boardTitleList = targetType==RedisEnum.STORE.label() ?  getAllTokenizer(title) : getNTokenizer(title);

            for (String item : boardTitleList) {
                if (resultMap.containsKey(item)) {
                    resultMap.get(item).add(id);  // 이미 있는 키에 대해 아이디를 추가
                } else {
                    List<Long> idList = new ArrayList<>();
                    idList.add(id);
                    resultMap.put(item, idList);  // 새로운 키에 대해 새로운 아이디 리스트 생성
                }
            }
        }

        if (targetType == RedisEnum.BOARD.label()){
            resultMap.put(MIGRATION, List.of(0L));
        }

        return resultMap;
    }

    private void uploadRedis(Map<String, List<Long>> resultMap, String targetType){
        for (Map.Entry<String, List<Long>> entry : resultMap.entrySet()) {
            redisRepository.set(targetType,entry.getKey(),
                    entry.getValue()
                            .stream()
                            .map(id -> id.toString())
                            .toArray(String[]::new));
        }
    }


    private KomoranResult getTokenizer(String title) {
        return KomoranUtil.getInstance().analyze(title);
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
    public List<String> getBestKeyword() {
        return redisRepository.getStringList(
                RedisEnum.BEST_KEYWORD.label(),
                BEST_KEYWORD_KEY
        );
    }

    @Override
    public List<String> getAutoKeyword(String keyword) {
        return trie.autoComplete(keyword, 7);
    }

    private List<String> getNTokenizer(String title){
        return  getTokenizer(title).getMorphesByTags("NNG", "NNP", "NNB", "NP", "NR", "NA");
    }

    private List<String> getAllTokenizer(String title) {
        System.out.println(getTokenizer(title).getTokenList());
        return getTokenizer(title).getTokenList().stream().map(token-> {
            System.out.println(token.getMorph());
            return token.getMorph();
        }).toList();
    }
}
