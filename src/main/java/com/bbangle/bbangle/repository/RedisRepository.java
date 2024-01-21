package com.bbangle.bbangle.repository;

import java.util.List;

public interface RedisRepository {
    List<Long> get(String key);
    Boolean set(String key, String... values);
}
