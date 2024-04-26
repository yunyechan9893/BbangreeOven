package com.bbangle.bbangle.config.ranking;

import com.bbangle.bbangle.ranking.service.RankingService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BoardWishListConfig {

    private final RankingService rankingService;

    @PostConstruct
    public void init() {
        rankingService.updatingNonRankedBoards();
    }

}
