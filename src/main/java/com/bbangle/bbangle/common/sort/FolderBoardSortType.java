package com.bbangle.bbangle.common.sort;

import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.board.repository.folder.cursor.CursorGenerator;
import com.bbangle.bbangle.board.repository.folder.cursor.LowPriceCursorGenerator;
import com.bbangle.bbangle.board.repository.folder.cursor.PopularCursorGenerator;
import com.bbangle.bbangle.board.repository.folder.cursor.WishListRecentCursorGenerator;
import com.bbangle.bbangle.board.repository.folder.query.LowPriceBoardQueryProvider;
import com.bbangle.bbangle.board.repository.folder.query.PopularBoardQueryProvider;
import com.bbangle.bbangle.board.repository.folder.query.QueryGenerator;
import com.bbangle.bbangle.board.repository.folder.query.WishListRecentBoardQueryProvider;
import com.bbangle.bbangle.ranking.domain.QRanking;
import com.bbangle.bbangle.wishlist.domain.QWishListBoard;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Getter;

@Getter
public enum FolderBoardSortType {
    LOW_PRICE(
        QBoard.board.price::asc,
        LowPriceCursorGenerator::new,
        LowPriceBoardQueryProvider::new),
    POPULAR(
        QRanking.ranking.popularScore::desc,
        PopularCursorGenerator::new,
        PopularBoardQueryProvider::new),
    WISHLIST_RECENT(
        QWishListBoard.wishListBoard.id::desc,
        WishListRecentCursorGenerator::new,
        WishListRecentBoardQueryProvider::new);

    private final Supplier<OrderSpecifier<?>> setOrder;
    private final Function<JPAQueryFactory, CursorGenerator> createCursor;
    private final Function<JPAQueryFactory, QueryGenerator> getQuery;

    FolderBoardSortType(Supplier<OrderSpecifier<?>> setOrder,
        Function<JPAQueryFactory, CursorGenerator> createCursor,
        Function<JPAQueryFactory, QueryGenerator> getQuery
    ) {
        this.setOrder = setOrder;
        this.createCursor = createCursor;
        this.getQuery = getQuery;
    }

    public CursorGenerator createCursor(JPAQueryFactory jpaQueryFactory){
        return createCursor.apply(jpaQueryFactory);
    }

    public OrderSpecifier<?> getOrderSpecifier(){
        return setOrder.get();
    }

    public QueryGenerator getBoards(JPAQueryFactory jpaQueryFactory){
        return getQuery.apply(jpaQueryFactory);
    }

}
