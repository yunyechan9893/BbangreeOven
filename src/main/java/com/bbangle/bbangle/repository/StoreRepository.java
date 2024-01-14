package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreQueryDSLRepository {

}
