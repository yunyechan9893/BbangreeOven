package com.bbangle.bbangle.analytics.dto;

import lombok.Builder;

@Builder
public record AnalyticsWishlistUsageRatioResponseDto(
        String wishlistUsageRatio
) {
}
