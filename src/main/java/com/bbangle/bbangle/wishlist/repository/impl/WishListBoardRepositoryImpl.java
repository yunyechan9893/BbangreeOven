package com.bbangle.bbangle.wishlist.repository.impl;

import com.bbangle.bbangle.wishlist.repository.WishListBoardQueryDSLRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.bbangle.bbangle.wishlist.domain.QWishListBoard.wishListBoard;

@Repository
@RequiredArgsConstructor
public class WishListBoardRepositoryImpl implements WishListBoardQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countMembersUsingWishlist() {
        Long result = queryFactory.selectDistinct(wishListBoard.memberId)
                .from(wishListBoard)
                .fetchOne();

        return result != null ? result : 0L;
    }

}
