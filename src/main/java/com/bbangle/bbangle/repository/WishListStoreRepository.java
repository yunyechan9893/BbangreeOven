package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.WishlistStore;
import com.bbangle.bbangle.repository.queryDsl.WishListStoreQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListStoreRepository extends JpaRepository<WishlistStore, Long>, WishListStoreQueryDSLRepository {
}
