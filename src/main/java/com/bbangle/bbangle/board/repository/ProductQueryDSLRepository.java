package com.bbangle.bbangle.board.repository;

import com.bbangle.bbangle.board.domain.Category;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductQueryDSLRepository {

    Map<Long, Set<Category>> getCategoryInfoByBoardId(List<Long> boardIds);
}