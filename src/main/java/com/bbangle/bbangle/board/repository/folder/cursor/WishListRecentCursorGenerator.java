package com.bbangle.bbangle.board.repository.folder.cursor;

import com.bbangle.bbangle.wishlist.domain.QWishListBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class WishListRecentCursorGenerator implements CursorGenerator{

    private static final QWishListBoard wishListBoard = QWishListBoard.wishListBoard;

    private final JPAQueryFactory queryFactory;
    private final Long memberId;

    @Override
    public BooleanBuilder getCursor(Long cursorId) {
        BooleanBuilder cursorBuilder = new BooleanBuilder();
        if(cursorId == null){
            return cursorBuilder;
        }
        Long wishListBoardId = queryFactory
            .select(wishListBoard.id)
            .from(wishListBoard)
            .where(wishListBoard.boardId.eq(cursorId).and(wishListBoard.memberId.eq(memberId)))
            .fetchOne();
        return cursorBuilder.and(wishListBoard.id.loe(wishListBoardId));
    }

}
