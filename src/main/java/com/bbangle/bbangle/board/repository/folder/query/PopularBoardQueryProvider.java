package com.bbangle.bbangle.board.repository.folder.query;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.board.domain.QProduct;
import com.bbangle.bbangle.board.dto.CursorInfo;
import com.bbangle.bbangle.common.sort.SortType;
import com.bbangle.bbangle.ranking.domain.QRanking;
import com.bbangle.bbangle.store.domain.QStore;
import com.bbangle.bbangle.wishlist.domain.QWishListBoard;
import com.bbangle.bbangle.wishlist.domain.QWishListFolder;
import com.bbangle.bbangle.wishlist.domain.WishListFolder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PopularBoardQueryProvider implements QueryGenerator{

    private static final QBoard board = QBoard.board;
    private static final QProduct product = QProduct.product;
    private static final QStore store = QStore.store;
    private static final QRanking ranking = QRanking.ranking;
    private static final QWishListBoard wishListBoard = QWishListBoard.wishListBoard;

    private static final int BOARD_PAGE_SIZE = 10;

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Board> getBoards(BooleanBuilder cursor, OrderSpecifier<?> order, WishListFolder folder) {
        List<Long> fetch = queryFactory
            .select(board.id)
            .distinct()
            .from(board)
            .join(wishListBoard)
            .on(board.id.eq(wishListBoard.board.id))
            .join(ranking)
            .on(board.id.eq(ranking.board.id))
            .where(wishListBoard.wishlistFolder.id.eq(folder.getId())
                .and(wishListBoard.isDeleted.eq(false))
                .and(cursor))
            .orderBy(order, board.id.desc())
            .limit(BOARD_PAGE_SIZE + 1L)
            .fetch();

        return queryFactory.select(board)
            .from(board)
            .leftJoin(board.productList, product)
            .fetchJoin()
            .join(store)
            .on(board.store.id.eq(store.id))
            .fetchJoin()
            .join(ranking)
            .fetchJoin()
            .on(board.id.eq(ranking.board.id))
            .where(board.id.in(fetch))
            .orderBy(order, board.id.desc())
            .fetch();
    }

}
