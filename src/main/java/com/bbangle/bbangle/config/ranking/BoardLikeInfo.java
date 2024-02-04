package com.bbangle.bbangle.config.ranking;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardLikeInfo {

    private final int score;
    private final LocalDateTime createdAt;

}
