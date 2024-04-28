package com.bbangle.bbangle.wishlist.controller;

import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.util.SecurityUtils;
import com.bbangle.bbangle.wishlist.dto.WishProductRequestDto;
import com.bbangle.bbangle.wishlist.service.WishListBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards/{boardId}/wish")
@RequiredArgsConstructor
public class WishListBoardController {

    private final WishListBoardService wishListBoardService;
    private final ResponseService responseService;

    @PostMapping
    public CommonResult wish(
        @PathVariable
        Long boardId,
        @RequestBody @Valid
        WishProductRequestDto wishRequest
    ) {
        Long memberId = SecurityUtils.getMemberId();
        wishListBoardService.wish(memberId, boardId, wishRequest);

        return responseService.getSuccessResult();
    }

    @PutMapping
    public CommonResult cancel(
        @PathVariable
        Long boardId
    ) {
        Long memberId = SecurityUtils.getMemberId();
        wishListBoardService.cancel(memberId, boardId);

        return responseService.getSuccessResult();
    }

}
