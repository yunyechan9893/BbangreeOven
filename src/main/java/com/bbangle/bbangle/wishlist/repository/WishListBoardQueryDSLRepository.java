package com.bbangle.bbangle.wishlist.repository;

import java.time.LocalDate;

public interface WishListBoardQueryDSLRepository {

    Long countMembersUsingWishlist();

    Long countWishlistByPeriod(LocalDate startDate, LocalDate endDate);
}
