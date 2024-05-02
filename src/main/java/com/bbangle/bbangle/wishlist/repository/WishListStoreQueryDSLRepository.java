package com.bbangle.bbangle.wishlist.repository;

import com.bbangle.bbangle.page.StoreCustomPage;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import com.bbangle.bbangle.wishlist.dto.WishListStoreCustomPage;
import com.bbangle.bbangle.wishlist.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.wishlist.domain.WishlistStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WishListStoreQueryDSLRepository {
    WishListStoreCustomPage<List<WishListStoreResponseDto>> getWishListStoreResponse(Long memberId, Long cursorId);
    Optional<WishlistStore> findWishListStore(Long memberId, Long storeId);
    List<WishlistStore> findWishListStores(Long memberId);

}
