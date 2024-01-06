package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
