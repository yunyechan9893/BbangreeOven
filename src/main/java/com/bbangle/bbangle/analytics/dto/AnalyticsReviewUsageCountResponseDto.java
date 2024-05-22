package com.bbangle.bbangle.analytics.dto;

import lombok.Builder;

@Builder
public record AnalyticsReviewUsageCountResponseDto(
        Long reviewCount
) {
}
