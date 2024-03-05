package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Board;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.repository.queryDsl.AdminBoardQueryDSLRepository;
import com.bbangle.bbangle.repository.queryDsl.AdminStoreQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminStoreRepository extends JpaRepository<Store, Long>, AdminStoreQueryDSLRepository {

}
