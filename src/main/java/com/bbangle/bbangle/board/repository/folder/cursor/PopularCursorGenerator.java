package com.bbangle.bbangle.board.repository.folder.cursor;

import com.bbangle.bbangle.board.domain.QBoard;
import com.bbangle.bbangle.ranking.domain.QRanking;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PopularCursorGenerator implements CursorGenerator{

    private static final QRanking ranking = QRanking.ranking;
    private static final QBoard board = QBoard.board;

    private final JPAQueryFactory queryFactory;

    @Override
    public BooleanBuilder getCursor(Long cursorId, Long memberId) {
        BooleanBuilder cursorBuilder = new BooleanBuilder();
        Double score = queryFactory
            .select(ranking.popularScore)
            .from(ranking)
            .where(ranking.board.id.eq(cursorId))
            .fetchOne();
        cursorBuilder.and(ranking.popularScore.loe(score)
            .and(board.id.lt(cursorId)));
        return cursorBuilder;
    }

}
