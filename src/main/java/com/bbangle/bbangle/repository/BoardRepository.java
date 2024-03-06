package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Board;
import com.bbangle.bbangle.repository.queryDsl.BoardQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryDSLRepository {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Board b SET b.detail = :detailUrl WHERE b.id = :boardId ")
    int updateDetailWhereStoreIdEqualsBoardId(
        @Param("boardId")
        Long boardId,
        @Param("detailUrl")
        String detailUrl
    );

}
