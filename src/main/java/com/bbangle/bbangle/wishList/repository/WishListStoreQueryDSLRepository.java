package com.bbangle.bbangle.wishList.repository;

import com.bbangle.bbangle.wishList.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.wishList.domain.WishListStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WishListStoreQueryDSLRepository {
    Page<WishListStoreResponseDto> getWishListStoreResponse(Long memberId, Pageable pageable);
    Optional<WishListStore> findWishListStore(Long memberId, Long storeId);
    List<WishListStore> findWishListStores(Long memberId);

}
