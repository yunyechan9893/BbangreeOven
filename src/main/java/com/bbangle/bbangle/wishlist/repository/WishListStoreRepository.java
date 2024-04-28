package com.bbangle.bbangle.wishlist.repository;

import com.bbangle.bbangle.wishlist.domain.WishlistStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListStoreRepository extends JpaRepository<WishlistStore, Long>, WishListStoreQueryDSLRepository {
}
