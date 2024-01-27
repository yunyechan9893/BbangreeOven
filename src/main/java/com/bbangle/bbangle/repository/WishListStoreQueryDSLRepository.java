package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.model.WishlistStore;

import java.util.List;
import java.util.Optional;

public interface WishListStoreQueryDSLRepository {
    List<WishListStoreResponseDto> getWishListStoreRes(Long memberId);
    Optional<WishlistStore> findWishListStore(Long memberId, Long storeId);

}
