package com.bbangle.bbangle.analytics.controller;


import com.bbangle.bbangle.analytics.dto.*;
import com.bbangle.bbangle.analytics.service.AnalyticsService;
import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final ResponseService responseService;
    private final AnalyticsService analyticsService;


    @GetMapping(value = "/members/count")
    public CommonResult getMembersCount() {
        AnalyticsMembersCountResponseDto response = AnalyticsMembersCountResponseDto.builder()
                .memberCount(analyticsService.countAllMember())
                .build();

        return responseService.getSingleResult(response);
    }


    @GetMapping(value = "/new-members/count")
    public CommonResult getNewMembersCount() {
        AnalyticsMembersCountResponseDto response = AnalyticsMembersCountResponseDto.builder()
                .memberCount(analyticsService.countNewMember())
                .build();

        return responseService.getSingleResult(response);
    }


    @GetMapping(value = "/ratio/wishlist-usage")
    public CommonResult getWishlistUsageRatio() {
        AnalyticsWishlistUsageRatioResponseDto response = AnalyticsWishlistUsageRatioResponseDto.builder()
                .wishlistUsageRatio(analyticsService.countMembersUsingWishlist())
                .build();

        return responseService.getSingleResult(response);
    }


    @GetMapping(value = "/wishlist/boards/ranking")
    public CommonResult getWishlistBoardRanking() {
        List<Board> response = analyticsService.getBoardWishlistRanking();

        return responseService.getListResult(response);
    }


    @GetMapping(value = "/wishlist/boards/count")
    public CommonResult getWishlistUsageCount(
            @RequestParam(value = "optStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> optStartDate,
            @RequestParam(value = "optEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> optEndDate
    ) {
        LocalDate startDate = optStartDate.orElse(LocalDate.now().minusDays(7));
        LocalDate endDate = optEndDate.orElse(LocalDate.now());

        AnalyticsWishlistUsageCountResponseDto response = AnalyticsWishlistUsageCountResponseDto.builder()
                .wishlistCount(analyticsService.countWishlistBoardByPeriod(startDate, endDate))
                .build();

        return responseService.getSingleResult(response);
    }


    @GetMapping(value = "/ratio/review-usage")
    public CommonResult getReviewUsageRatio() {
        AnalyticsRankingUsageRatioResponseDto response = AnalyticsRankingUsageRatioResponseDto.builder()
                .rankingUsageRatio(analyticsService.calculateReviewUsingRatio())
                .build();

        return responseService.getSingleResult(response);
    }


    @GetMapping(value = "/reviews/count")
    public CommonResult getReviewUsageRatio(
            @RequestParam(value = "optStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> optStartDate,
            @RequestParam(value = "optEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> optEndDate
    ) {
        LocalDate startDate = optStartDate.orElse(LocalDate.now().minusDays(7));
        LocalDate endDate = optEndDate.orElse(LocalDate.now());

        AnalyticsReviewUsageCountResponseDto response = AnalyticsReviewUsageCountResponseDto.builder()
                .reviewCount(analyticsService.countReviewByPeriod(startDate, endDate))
                .build();

        return responseService.getSingleResult(response);
    }

}
