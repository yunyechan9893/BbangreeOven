package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.WishListStoreResponseDto;

public interface WishListStoreService {
    WishListStoreResponseDto getWishListStoresRes(Long memberId);
}
