package com.bbangle.bbangle.wishlist.repository.impl;

import com.bbangle.bbangle.wishlist.repository.WishListBoardQueryDSLRepository;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static com.bbangle.bbangle.wishlist.domain.QWishListBoard.wishListBoard;

@Repository
@RequiredArgsConstructor
public class WishListBoardRepositoryImpl implements WishListBoardQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countMembersUsingWishlist() {
        return queryFactory.select(wishListBoard.memberId.countDistinct())
                .from(wishListBoard)
                .fetchOne();
    }

    @Override
    public Long countWishlistByPeriod(LocalDate startDate, LocalDate endDate) {
        DateTemplate<LocalDate> createdAt = Expressions.dateTemplate(LocalDate.class, "DATE({0})", wishListBoard.createdAt);

        return queryFactory.select(wishListBoard.id.count())
                .from(wishListBoard)
                .where(createdAt.between(startDate, endDate))
                .fetchOne();
    }
}
