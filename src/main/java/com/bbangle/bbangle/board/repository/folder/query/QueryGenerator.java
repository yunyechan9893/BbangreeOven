package com.bbangle.bbangle.board.repository.folder.query;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.wishlist.domain.WishListFolder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import java.util.List;

public interface QueryGenerator {

    List<Board> getBoards();

}
