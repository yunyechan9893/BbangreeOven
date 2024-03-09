package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Product;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.repository.queryDsl.AdminProductQueryDSLRepository;
import com.bbangle.bbangle.repository.queryDsl.AdminStoreQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductRepository extends JpaRepository<Product, Long>, AdminProductQueryDSLRepository {

}
