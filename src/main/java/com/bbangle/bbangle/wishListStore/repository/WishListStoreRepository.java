package com.bbangle.bbangle.wishListStore.repository;

import com.bbangle.bbangle.wishListStore.domain.WishlistStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListStoreRepository extends JpaRepository<WishlistStore, Long>, WishListStoreQueryDSLRepository {
}
