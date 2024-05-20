package com.bbangle.bbangle.store.controller;

import com.bbangle.bbangle.board.dto.StoreAllBoardDto;
import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.store.service.StoreService;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/stores")
public class StoreController {

    private final StoreService storeService;
    private final ResponseService responseService;

    @GetMapping
    public CommonResult getList(
        @RequestParam(required = false, value = "cursorId")
        Long cursorId,
        @AuthenticationPrincipal
        Long memberId
    ) {
        return responseService.getSingleResult(storeService.getList(cursorId, memberId));
    }

    @GetMapping("/{storeId}")
    public CommonResult getStoreDetailResponse(
        @PathVariable("storeId")
        Long storeId
    ) {
        Long memberId = SecurityUtils.getMemberIdWithAnonymous();
        return responseService.getSingleResult(
            storeService.getStoreDetailResponse(memberId, storeId)
        );
    }

    @GetMapping("/{storeId}/boards/all")
    public CommonResult getAllBoard(
        @RequestParam("page")
        int page,
        @PathVariable("storeId")
        Long storeId
    ) {
        Long memberId = SecurityUtils.getMemberIdWithAnonymous();

        SliceImpl<StoreAllBoardDto> storeAllBoardDtos = storeService.getAllBoard(page, memberId,
            storeId);
        return responseService.getSingleResult(storeAllBoardDtos);
    }
}
