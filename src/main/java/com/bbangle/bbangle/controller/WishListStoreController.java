package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.config.jwt.TokenProvider;
import com.bbangle.bbangle.dto.MessageResDto;
import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.service.impl.WishListStoreServiceImpl;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("likes")
public class WishListStoreController {
    private final TokenProvider tokenProvider;
    private final WishListStoreServiceImpl wishlistStoreService;

    /**
     * 스토어 위시리스트 조회
     *
     * @return the response entity 메세지
     */
    @GetMapping("/stores")
    public ResponseEntity<List<WishListStoreResponseDto>> getWishListStores(){
        Long memberId = SecurityUtils.getMemberId();
        List<WishListStoreResponseDto> wishListStoreResList = wishlistStoreService.getWishListStoresRes(memberId);
        return ResponseEntity.ok().body(wishListStoreResList);
    }

    @PostMapping("/store/{storeId}")
    public ResponseEntity<MessageResDto> addWishListStore(@PathVariable Long storeId){
        Long memberId = SecurityUtils.getMemberId();
        try {
            wishlistStoreService.save(memberId, storeId);
            return ResponseEntity.ok().body(new MessageResDto("success"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResDto(e.getMessage()));
        }
    }
    
}
