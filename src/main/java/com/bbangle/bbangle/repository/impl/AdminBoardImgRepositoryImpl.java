package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.model.QProductImg;
import com.bbangle.bbangle.repository.queryDsl.AdminBoardImgQueryDSLRepository;
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
