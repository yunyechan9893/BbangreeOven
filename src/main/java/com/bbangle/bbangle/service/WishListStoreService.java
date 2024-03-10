package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.WishListStorePagingDto;
import org.springframework.data.domain.Pageable;

public interface WishListStoreService {
    WishListStorePagingDto getWishListStoresRes(Long memberId, Pageable pageable);

    void deletedByDeletedMember(Long memberId);
}
