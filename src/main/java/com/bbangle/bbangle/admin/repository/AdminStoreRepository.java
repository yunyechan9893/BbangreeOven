package com.bbangle.bbangle.admin.repository;

import com.bbangle.bbangle.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminStoreRepository extends JpaRepository<Store, Long>, AdminStoreQueryDSLRepository {

}
