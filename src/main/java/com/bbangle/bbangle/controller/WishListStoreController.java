package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.MessageResDto;
import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.service.impl.WishListStoreServiceImpl;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class WishListStoreController {

    private final WishListStoreServiceImpl wishlistStoreService;

    /**
     * 스토어 위시리스트 조회
     *
     * @return the response entity List<wishListStoreResDto>
     */
    @GetMapping("/stores")
    public ResponseEntity<List<WishListStoreResponseDto>> getWishListStores() {
        Long memberId = SecurityUtils.getMemberId();
        List<WishListStoreResponseDto> wishListStoreResList = wishlistStoreService.getWishListStoresRes(
            memberId);
        return ResponseEntity.ok()
            .body(wishListStoreResList);
    }

    /**
     * 스토어 위시리스트 추가
     *
     * @param storeId 스토어 id
     * @return 메세지
     * @hidden memberId 멤버 id
     */
    @PostMapping("/store/{storeId}")
    public ResponseEntity<MessageResDto> addWishListStore(
        @PathVariable
        Long storeId
    ) {
        Long memberId = SecurityUtils.getMemberId();
        try {
            wishlistStoreService.save(memberId, storeId);
            return ResponseEntity.ok()
                .body(new MessageResDto("스토어를 찜했습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResDto("스토어를 찜하지 못했습니다"));
        }
    }

    /**
     * 스토어 위시리스트 삭제
     *
     * @param storeId the store id
     * @return the response entity
     */
    @PatchMapping("/store/{storeId}")
    public ResponseEntity<MessageResDto> deleteWishListStore(
        @PathVariable
        Long storeId
    ) {
        Long memberId = SecurityUtils.getMemberId();
        wishlistStoreService.deleteStore(memberId, storeId);
        return ResponseEntity.ok()
            .body(new MessageResDto("스토어 찜을 해제했습니다"));
    }

}
