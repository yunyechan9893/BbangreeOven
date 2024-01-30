package com.bbangle.bbangle.repository.impl;

import java.util.List;
import com.bbangle.bbangle.model.QBoard;
import com.bbangle.bbangle.model.QWishlistProduct;
import com.bbangle.bbangle.model.SortType;
import com.bbangle.bbangle.model.WishlistFolder;
import com.bbangle.bbangle.model.WishlistProduct;
import com.bbangle.bbangle.repository.queryDsl.WishListProductQueryDSLRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WishListProductRepositoryImpl implements WishListProductQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WishlistProduct> findAllByWishlistFolder(WishlistFolder selectedFolder, String sort) {
        QWishlistProduct products = QWishlistProduct.wishlistProduct;
        QBoard board = QBoard.board;

        OrderSpecifier<?> orderSpecifier = sortType(sort, board, products);

        return queryFactory.select(products)
            .from(products)
            .leftJoin(products.board, board)
            .where(products.wishlistFolder.eq(selectedFolder))
            .orderBy(orderSpecifier)
            .fetch();
    }

    private static OrderSpecifier<?> sortType(String sort, QBoard board, QWishlistProduct products) {
        OrderSpecifier<?> orderSpecifier;
        switch (SortType.fromString(sort)) {
            case RECENT:
                orderSpecifier = products.createdAt.desc();
                break;
            case LOW_PRICE:
                orderSpecifier = board.price.asc();
                break;
            case POPULAR:
                orderSpecifier = board.wishCnt.desc();
                break;
            default:
                throw new IllegalArgumentException("Invalid SortType");
        }
        return orderSpecifier;
    }
}
