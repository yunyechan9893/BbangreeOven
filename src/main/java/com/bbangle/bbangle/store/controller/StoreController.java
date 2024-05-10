package com.bbangle.bbangle.store.controller;

import com.bbangle.bbangle.page.StoreDetailCustomPage;
import com.bbangle.bbangle.store.dto.PopularBoardResponse;
import com.bbangle.bbangle.store.dto.StoreBoardsResponse;
import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.store.dto.StoreResponse;
import com.bbangle.bbangle.store.service.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
        Long cursorId
    ) {
        return responseService.getSingleResult(storeService.getList(cursorId));
    }

    @GetMapping("/{storeId}")
    public CommonResult getPopularBoardResponse(
        @PathVariable("storeId")
        Long storeId,
        @AuthenticationPrincipal
        Long memberId
    ) {
        StoreResponse storeDetailResponse = storeService.getStoreResponse(memberId, storeId);
        return responseService.getSingleResult(storeDetailResponse);
    }

    @GetMapping("/{storeId}/boards/best")
    public CommonResult getPopularBoardResponses(
        @PathVariable("storeId")
        Long storeId,
        @AuthenticationPrincipal
        Long memberId
    ) {
        List<PopularBoardResponse> popularBoardResponses = storeService.getPopularBoardResponse(
            memberId, storeId);
        return responseService.getSingleResult(popularBoardResponses);
    }

    @GetMapping("/{storeId}/boards")
    public CommonResult getStoreAllBoard(
        @PathVariable("storeId")
        Long storeId,
        @RequestParam(value = "cursorId", required = false)
        Long boardIdAsCursorId,
        @AuthenticationPrincipal
        Long memberId
    ) {
        StoreDetailCustomPage<List<StoreBoardsResponse>> storeAllBoardDtos = storeService.getStoreAllBoard(
            memberId, storeId, boardIdAsCursorId);
        return responseService.getSingleResult(storeAllBoardDtos);
    }
}
