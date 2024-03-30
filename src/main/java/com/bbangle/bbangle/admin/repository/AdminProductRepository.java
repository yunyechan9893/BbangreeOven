package com.bbangle.bbangle.admin.repository;

import com.bbangle.bbangle.board.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductRepository extends JpaRepository<Product, Long>, AdminProductQueryDSLRepository {

}
