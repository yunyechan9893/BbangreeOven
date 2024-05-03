package com.bbangle.bbangle.wishList.repository;

import com.bbangle.bbangle.wishList.domain.WishListStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListStoreRepository extends JpaRepository<WishListStore, Long>, WishListStoreQueryDSLRepository {
}
