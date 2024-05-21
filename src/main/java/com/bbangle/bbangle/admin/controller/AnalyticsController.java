package com.bbangle.bbangle.admin.controller;


import com.bbangle.bbangle.admin.service.AnalyticsService;
import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final ResponseService responseService;
    private final AnalyticsService analyticsService;


    @GetMapping(value = "/members")
    public CommonResult getMembersCount() {
        Long memberCount = analyticsService.countAllMember();
        return responseService.getSingleResult(memberCount);
    }


    @GetMapping(value = "/new-members")
    public CommonResult getNewMembersCount() {
        Long newMemberCount = analyticsService.countNewMember();
        return responseService.getSingleResult(newMemberCount);
    }

    @GetMapping(value = "/wishlist-usage-ratio")
    public CommonResult getWishlistUsageRatio() {
        Double wishlistUsageRatio = analyticsService.countMembersUsingWishlist();
        return responseService.getSingleResult(wishlistUsageRatio);
    }

    @GetMapping(value = "/wishlist-ranking/boards")
    public CommonResult getWishlistBoardRanking() {

        return null;
    }

    @GetMapping(value = "/wishlist-usage/boards/{period}")
    public CommonResult getWishlistUsageCount(
            @PathVariable("period") int period
    ) {

        return null;
    }

    @GetMapping(value = "review-usage-ratio")
    public CommonResult getReviewUsageRatio() {

        return null;
    }

    @GetMapping(value = "/review-usage/{period}")
    public CommonResult getReviewUsageRatio(
            @PathVariable("period") int period
    )  {

        return null;
    }

}
