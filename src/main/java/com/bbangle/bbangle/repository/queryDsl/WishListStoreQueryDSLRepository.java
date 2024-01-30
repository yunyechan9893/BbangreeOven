package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.WishListStoreResponseDto;

import java.util.List;

public interface WishListStoreQueryDSLRepository {
    List<WishListStoreResponseDto> getWishListStoreRes(Long memberId);
}
