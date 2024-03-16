package com.bbangle.bbangle.admin.repository;

import com.bbangle.bbangle.board.domain.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBoardImgRepository extends JpaRepository<ProductImg, Long>, AdminBoardImgQueryDSLRepository {

}
