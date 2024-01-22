package com.bbangle.bbangle.scripts;

import com.bbangle.bbangle.repository.InitRepository;
import com.bbangle.bbangle.repository.RedisRepository;
import com.bbangle.bbangle.repository.SearchRepository;
import com.bbangle.bbangle.service.SearchService;
import com.bbangle.bbangle.util.KomoranUtil;
import jakarta.annotation.PostConstruct;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

@Component
public class InitialDataLoader {
    private final String MIGRATION="migration";
    private final String BOARD="board";
    private final String STORE="store";

    @Autowired
    InitRepository initRepository;

    @Autowired
    RedisRepository redisRepository;

    @PostConstruct
    public void loadData() {

        try{
            if (!redisRepository.get(BOARD, MIGRATION).isEmpty()){
                System.out.println("[완료] 이미 동기화가 되어 있습니다");
                return;
            }
        } catch (Exception e) {
            System.out.println("[에러] 레디스 서버 장애가 발생했습니다");
            return;
        }


        System.out.println("[완료] 보드 동기화 중");
        HashMap<Long, String> boardTitles = initRepository.getAllBoardTitle();
        uploadRedis(boardTitles, BOARD);

        System.out.println("[완료] 스토어 동기화 중");
        HashMap<Long, String> storeTitles = initRepository.getAllStoreTitle();
        uploadRedis(storeTitles, STORE);

        System.out.println("[완료] 레디스에 동기화 완료");
    }

    private void uploadRedis(HashMap<Long, String> targetTitles, String targetType){
        Map<String, List<Long>> resultMap = new HashMap<>();

        for (Map.Entry<Long, String> entry : targetTitles.entrySet()) {
            Long id = entry.getKey();
            String title = entry.getValue();
            List<String> boardTitleList = targetType==STORE ?  getAllTokenizer(title) : getNTokenizer(title);

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

        if (targetType == BOARD){
            resultMap.put(MIGRATION, List.of(0L));
        }

        for (Map.Entry<String, List<Long>> entry : resultMap.entrySet()) {
            Boolean result = redisRepository.set(targetType,entry.getKey(),
                    entry.getValue()
                            .stream()
                            .map(id -> id.toString())
                            .toArray(String[]::new));

        }
    }

    private KomoranResult getTokenizer(String title) {
        var komoran = KomoranUtil.getInstance();
        return komoran.analyze(title);
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
