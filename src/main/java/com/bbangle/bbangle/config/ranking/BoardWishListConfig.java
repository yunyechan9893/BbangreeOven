package com.bbangle.bbangle.config.ranking;

import com.bbangle.bbangle.repository.BoardRepository;
import com.bbangle.bbangle.util.RedisKeyUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class BoardWishListConfig {

    private final RedisTemplate<String, Object> redisTemplate;
    private final BoardRepository boardRepository;

    @PostConstruct
    public void init() {
        boardRepository.findAll()
            .forEach(board -> {
                redisTemplate.opsForZSet().add(RedisKeyUtil.RECOMMEND_KEY, board.getId(), 0);
                redisTemplate.opsForZSet().add(RedisKeyUtil.POPULAR_KEY, board.getId(), 0);
            });
    }

}
