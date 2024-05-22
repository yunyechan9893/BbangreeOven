package com.bbangle.bbangle.review.repository;

import java.time.LocalDate;

public interface ReviewQueryDSLRepository {

    Long countMembersUsingReview();

    Long countReviewByPeriod(LocalDate startDate, LocalDate endDate);
}
