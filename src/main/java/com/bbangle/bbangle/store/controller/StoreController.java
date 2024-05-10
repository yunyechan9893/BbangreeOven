package com.bbangle.bbangle.store.controller;

import com.bbangle.bbangle.board.dto.StoreAllBoardDto;
import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.store.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.store.service.StoreService;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/stores")
public class StoreController {

    private final StoreService storeService;
    private final ResponseService responseService;

    @GetMapping
    public CommonResult getList(
        @RequestParam(required = false)
        Long cursorId,
        @AuthenticationPrincipal
        Long memberId
    ) {
        return responseService.getSingleResult(storeService.getList(cursorId, memberId));
    }

    @GetMapping("/{id}")
    public CommonResult getStoreDetailResponse(
            @PathVariable("id")
            Long storeId
    ){
        Long memberId = SecurityUtils.getMemberIdWithAnonymous();

        StoreDetailResponseDto storeDetailResponse = storeService.getStoreDetailResponse(memberId, storeId);
        return responseService.getSingleResult(storeDetailResponse);
    }

    @GetMapping("/{id}/boards/all")
    public CommonResult getAllBoard(
            @RequestParam("page")
            int page,
            @PathVariable("id")
            Long storeId
    ){
        Long memberId = SecurityUtils.getMemberIdWithAnonymous();

        SliceImpl<StoreAllBoardDto> storeAllBoardDtos = storeService.getAllBoard(page, memberId, storeId);
        return responseService.getSingleResult(storeAllBoardDtos);
    }
}
