package com.bbangle.bbangle.admin.repository;

import com.bbangle.bbangle.board.domain.QProductImg;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminBoardImgRepositoryImpl implements AdminBoardImgQueryDSLRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public int getBoardImageCount(Long boardId) {
        QProductImg productImg = QProductImg.productImg;

        return jpaQueryFactory
                .select(productImg.id.count())
                .from(productImg)
                .where(productImg.board.id.eq(boardId))
                .fetchOne().intValue();
    }
}
