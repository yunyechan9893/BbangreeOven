package com.bbangle.bbangle.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate; // 추가

import java.util.stream.Collectors;

public class DeduplicationUtils {
    // DTO 중복 제거 클래스
    // 사용방법
    // DeduplicationUtils.deduplication(List<DTO>, DTO::property)

    public static <T> List<T> deduplication(final List<T> list, Function<? super T, ?> key) {
        return list.stream().filter(deduplicationPredicate(key)) // 수정
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> deduplicationPredicate(Function<? super T, ?> key) { // 수정
        final Set<Object> set = ConcurrentHashMap.newKeySet();
        System.out.println(key);
        return predicate -> set.add(key.apply(predicate));
    }
}