package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.ProductImg;
import com.bbangle.bbangle.repository.queryDsl.AdminBoardImgQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBoardImgRepository extends JpaRepository<ProductImg, Long>, AdminBoardImgQueryDSLRepository {

}
