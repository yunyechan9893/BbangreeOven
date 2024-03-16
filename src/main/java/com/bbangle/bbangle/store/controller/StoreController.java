package com.bbangle.bbangle.store.controller;

import com.bbangle.bbangle.board.dto.StoreAllBoardDto;
import com.bbangle.bbangle.store.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import com.bbangle.bbangle.store.service.StoreService;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<Slice<StoreResponseDto>> getList(
        @PageableDefault
        Pageable pageable
    ) {
        return ResponseEntity.ok(storeService.getList(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDetailResponseDto> getStoreDetailResponse(
            @PathVariable("id")
            Long storeId
    ){
        Long memberId = SecurityUtils.getMemberIdWithAnonymous();

        StoreDetailResponseDto storeDetailResponse = storeService.getStoreDetailResponse(memberId, storeId);
        ResponseEntity<StoreDetailResponseDto> response = ResponseEntity.ok().body(storeDetailResponse);
        return response;
    }

    @GetMapping("/{id}/boards/all")
    public ResponseEntity<SliceImpl<StoreAllBoardDto>> getAllBoard(
            @RequestParam("page")
            int page,
            @PathVariable("id")
            Long storeId
    ){
        Long memberId = SecurityUtils.getMemberIdWithAnonymous();

        SliceImpl<StoreAllBoardDto> storeAllBoardDtos = storeService.getAllBoard(page, memberId, storeId);
        return ResponseEntity.ok().body(storeAllBoardDtos);
    }
}
