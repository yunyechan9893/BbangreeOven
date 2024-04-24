package com.bbangle.bbangle.wishList.repository;

import com.bbangle.bbangle.wishList.domain.WishlistStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListStoreRepository extends JpaRepository<WishlistStore, Long>, WishListStoreQueryDSLRepository {
}
