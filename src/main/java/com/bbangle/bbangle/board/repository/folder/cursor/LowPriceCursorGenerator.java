package com.bbangle.bbangle.board.repository.folder.cursor;

import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LowPriceCursorGenerator implements CursorGenerator{

    private static final QBoard board = QBoard.board;

    private final JPAQueryFactory queryFactory;

    @Override
    public BooleanBuilder getCursor(Long cursorId) {
        BooleanBuilder cursorBuilder = new BooleanBuilder();
        if(cursorId == null){
            return cursorBuilder;
        }

        Optional.ofNullable(queryFactory.select(board.id)
            .from(board)
            .where(board.id.eq(cursorId))
            .fetchOne())
            .orElseThrow(() -> new BbangleException(BbangleErrorCode.BOARD_NOT_FOUND));

        Integer price = queryFactory
            .select(board.price)
            .from(board)
            .where(board.id.eq(cursorId))
            .fetchOne();
        cursorBuilder.and(board.price.goe(price)
            .and(board.id.loe(cursorId)));
        return cursorBuilder;
    }

}
