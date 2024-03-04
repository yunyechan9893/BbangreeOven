package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import java.util.List;

public interface WishListStoreService {

    List<WishListStoreResponseDto> getWishListStoresRes(Long memberId);

}
