package com.bbangle.bbangle.common.sort;

import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.board.repository.folder.cursor.CursorGenerator;
import com.bbangle.bbangle.board.repository.folder.cursor.LowPriceCursorGenerator;
import com.bbangle.bbangle.board.repository.folder.cursor.PopularCursorGenerator;
import com.bbangle.bbangle.board.repository.folder.cursor.WishListRecentCursorGenerator;
import com.bbangle.bbangle.ranking.domain.QRanking;
import com.bbangle.bbangle.wishlist.domain.QWishListBoard;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Getter;

@Getter
public enum SortType {
    RECENT(QBoard.board.createdAt::desc),
    POPULAR(QRanking.ranking.popularScore::desc);

    private final Supplier<OrderSpecifier<?>> setOrder;

    SortType(Supplier<OrderSpecifier<?>> setOrder
    ) {
        this.setOrder = setOrder;
    }

    public OrderSpecifier<?> getOrderSpecifier(){
        return setOrder.get();
    }

}
