package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.dto.BoardDto;
import com.bbangle.bbangle.dto.ProductDto;
import com.bbangle.bbangle.dto.StoreDto;
import com.bbangle.bbangle.model.Board;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.repository.queryDsl.AdminBoardQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBoardRepository extends JpaRepository<Board, Long>, AdminBoardQueryDSLRepository {


}
