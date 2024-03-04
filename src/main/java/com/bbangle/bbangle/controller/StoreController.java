package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.dto.StoreResponseDto;
import com.bbangle.bbangle.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ) {
        StoreDetailResponseDto storeDetailResponse = storeService.getStoreDetailResponse(storeId);
        ResponseEntity<StoreDetailResponseDto> response = ResponseEntity.ok()
            .body(storeDetailResponse);
        return response;
    }

}
