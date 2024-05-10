package com.bbangle.bbangle.store.controller;

import com.bbangle.bbangle.page.StoreDetailCustomPage;
import com.bbangle.bbangle.store.dto.PopularBoardResponse;
import com.bbangle.bbangle.store.dto.StoreBoardsResponse;
import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.store.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.store.dto.StoreResponse;
import com.bbangle.bbangle.store.service.StoreService;
import com.bbangle.bbangle.util.SecurityUtils;
import java.util.List;
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

    @GetMapping("/{storeId}")
    public CommonResult getPopularBoardResponse(
        @PathVariable("storeId")
        Long storeId,
        @AuthenticationPrincipal
        Long memberId
    ) {
        StoreResponse getStoreResponse = storeService.getStoreResponse(memberId, storeId);
        return responseService.getSingleResult(getStoreResponse);
    }

    @GetMapping("/{storeId}/boards/best")
    public CommonResult getPopularBoardResponses(
        @PathVariable("storeId")
        Long storeId,
        @AuthenticationPrincipal
        Long memberId
    ) {
        List<PopularBoardResponse> popularBoardResponses = storeService.getPopularBoardResponses(
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
        StoreDetailCustomPage<List<StoreBoardsResponse>> StoreBoardsResponses = storeService.getStoreAllBoard(
            memberId, storeId, boardIdAsCursorId);
        return responseService.getSingleResult(StoreBoardsResponses);
    }
}
