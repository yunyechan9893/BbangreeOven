package com.bbangle.bbangle.common.sort;

import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.ranking.domain.QRanking;
import com.bbangle.bbangle.wishlist.domain.QWishListBoard;
import com.querydsl.core.types.OrderSpecifier;
import java.util.function.Supplier;
import lombok.Getter;

@Getter
public enum SortType {
    RECENT(QBoard.board.createdAt::desc),
    LOW_PRICE(QBoard.board.price::asc),
    POPULAR(QRanking.ranking.popularScore::desc),
    WISHLIST_RECENT(QWishListBoard.wishListBoard.id::desc),
    RECOMMEND(QRanking.ranking.recommendScore::desc);

    SortType(Supplier<OrderSpecifier<?>> setOrder) {
        this.setOrder = setOrder;
    }
    private final Supplier<OrderSpecifier<?>> setOrder;

    public OrderSpecifier<?> getOrderSpecifier(){
        return setOrder.get();
    }
}
