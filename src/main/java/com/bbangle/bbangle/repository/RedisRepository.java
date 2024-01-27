package com.bbangle.bbangle.repository;

import java.util.List;

public interface RedisRepository {
    List<Long> get(String namespace, String key);
    Boolean set(String namespace, String key, String... values);
}
