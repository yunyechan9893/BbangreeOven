package com.bbangle.bbangle.config.ranking;

import com.bbangle.bbangle.util.RedisKeyUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankingUpdate {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");
    @Autowired
    @Qualifier("boardLikeInfoRedisTemplate")
    private final RedisTemplate<String, Object> boardLikeInfoRedisTemplate;
    @Qualifier("defaultRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    @Async
    @Scheduled(cron = "0 0 * * * *") // 매시 정각마다 실행
    public void cleanupLikes() {
        LocalDateTime updateDate = LocalDateTime.now()
            .minusDays(1);

        Set<String> keys = boardLikeInfoRedisTemplate.keys("*");
        if (keys != null) {
            for (String key : keys) {
                // 키의 시간 부분 파싱
                LocalDateTime keyTime;
                try {
                    keyTime = LocalDateTime.parse(key, formatter);
                } catch (Exception e) {
                    // 키 형식이 일치하지 않으면 다음 키로 넘어감
                    continue;
                }
                // 현재 시간으로부터 24시간 이전인 경우 해당 키 삭제
                if (keyTime.isBefore(updateDate) || keyTime.isEqual(updateDate)) {
                    List<Object> range = boardLikeInfoRedisTemplate.opsForList()
                        .range(keyTime.format(formatter), 0, -1);
                    if (range == null) {
                        continue;
                    }

                    for (Object ele : range) {
                        BoardLikeInfo info = (BoardLikeInfo) ele;
                        if (info.scoreType() == ScoreType.WISH) {
                            redisTemplate.opsForZSet()
                                .incrementScore(RedisKeyUtil.POPULAR_KEY,
                                    String.valueOf(info.boardId()), -info.score());
                            redisTemplate.opsForZSet()
                                .incrementScore(RedisKeyUtil.RECOMMEND_KEY,
                                    String.valueOf(info.boardId()), -info.score());
                            continue;
                        }
                        redisTemplate.opsForZSet()
                            .incrementScore(RedisKeyUtil.POPULAR_KEY,
                                String.valueOf(info.boardId()), -info.score());
                    }
                    boardLikeInfoRedisTemplate.delete(key);
                }
            }
        }

    }

}
