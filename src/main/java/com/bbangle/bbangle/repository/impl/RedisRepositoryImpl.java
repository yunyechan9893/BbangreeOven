package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.repository.RedisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public RedisRepositoryImpl(
            RedisTemplate<String, Object> redisTemplate,
            StringRedisTemplate stringRedisTemplate,
            ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Long> get(String namespace, String key) {
        try {
            // 가져온 값들을 List<Long>로 역직렬화
            String master = namespace + ":" + key;
            return redisTemplate.opsForList().range(master, 0, -1)
                    .stream()
                    .map(o ->Long.parseLong(o.toString()))
                    .toList();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Boolean set(String namespace, String key, String... values) {
        try {
            String master = namespace + ":" + key;
            System.out.println(master);
            redisTemplate.opsForList().rightPushAll(master, (Object[]) values);
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
}
