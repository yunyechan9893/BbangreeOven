package com.bbangle.bbangle.wishList.controller;

import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.wishList.dto.WishListStorePagingDto;
import com.bbangle.bbangle.wishList.service.WishListStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class WishListStoreController {
    private final WishListStoreService wishlistStoreService;
    private final ResponseService responseService;

    @GetMapping("/stores")
    public CommonResult getWishListStores(Pageable pageable,
        @AuthenticationPrincipal Long memberId){
        WishListStorePagingDto wishListStoresRes = wishlistStoreService.getWishListStoresResponse(memberId, pageable);
        return responseService.getSingleResult(wishListStoresRes);
    }

    @PostMapping("/store/{storeId}")
    public CommonResult addWishListStore(@PathVariable Long storeId,
        @AuthenticationPrincipal Long memberId){
        try {
            wishlistStoreService.save(memberId, storeId);
            return responseService.getSuccessResult("스토어를 찜했습니다", 0);
        }catch (Exception e){
            return responseService.getFailResult("스토어를 찜하지 못했습니다", -1);
        }
    }

    @PatchMapping("/store/{storeId}")
    public CommonResult deleteWishListStore(@PathVariable Long storeId,
        @AuthenticationPrincipal Long memberId){
         wishlistStoreService.deleteStore(memberId, storeId);
         return responseService.getSuccessResult("스토어 찜을 해제했습니다", 0);
    }
}
