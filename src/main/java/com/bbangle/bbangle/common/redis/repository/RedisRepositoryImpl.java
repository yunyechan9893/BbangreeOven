package com.bbangle.bbangle.common.redis.repository;

import com.bbangle.bbangle.common.redis.repository.RedisRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    @Qualifier("defaultRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Long> get(String namespace, String key) {
        String multiKey = String.format("%s:%s", namespace, key);
        return redisTemplate.opsForList()
            .range(multiKey, 0, -1)
            .stream()
            .map(o -> Long.parseLong(o.toString()))
            .toList();
    }

    @Override
    public List<String> getStringList(String namespace, String key) {
        // 네임스페이스와 키를 결합
        String multiKey = String.format("%s:%s", namespace, key);
        // multiKey를 이용해 레디스 조회 후 값 반환
        return redisTemplate.opsForList()
            .range(multiKey, 0, -1)
            .stream()
            .map(Object::toString)
            .toList();
    }

    @Override
    public String getString(String namespace, String key) {
        String multiKey = String.format("%s:%s", namespace, key);
        var value = redisTemplate.opsForValue();

        try {
            log.info(multiKey);
            log.info((String) value.get(multiKey));
            return value.get(multiKey)
                .toString();
        } catch (RedisSystemException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public void set(String namespace, String key, String... values) {
        // 네임스페이스와 키를 결합
        String multiKey = String.format("%s:%s", namespace, key);
        redisTemplate.opsForList()
            .rightPushAll(multiKey, values);
        log.info("[완료] 레디스 값 저장");
    }

    @Override
    public void setFromString(String namespace, String key, String value) {
        // 네임스페이스와 키를 결합
        String multiKey = String.format("%s:%s", namespace, key);
        redisTemplate.opsForValue()
            .set(multiKey, value);
        log.info("[완료] 레디스 값 저장");
    }

    @Override
    public void delete(String namespace, String key) {
        String multiKey = String.format("%s:%s", namespace, key);
        redisTemplate.delete(multiKey);
        log.info("[완료] 레디스 값 삭제");
    }

    @Override
    public void deleteAll() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

}
