package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.model.WishlistStore;

import java.util.List;

public interface WishListStoreQueryDSLRepository {
    List<WishListStoreResponseDto> getWishListStoreRes(Long memberId);
    WishlistStore findWishListStore(Long memberId, Long storeId);

}
