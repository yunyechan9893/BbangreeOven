package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.model.WishlistStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface WishListStoreQueryDSLRepository {
    Page<WishListStoreResponseDto> getWishListStoreRes(Long memberId, Pageable pageable);
    Optional<WishlistStore> findWishListStore(Long memberId, Long storeId);

}
