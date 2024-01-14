package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/stores")
public class StoreController {

    @Autowired
    StoreService storeService;
    @GetMapping("/{id}")
    public ResponseEntity<StoreDetailResponseDto> getStoreDetailResponse(
            @PathVariable("id")
            Long storeId
    ){
        StoreDetailResponseDto storeDetailResponse = storeService.getStoreDetailResponse(storeId);
        ResponseEntity<StoreDetailResponseDto> response = ResponseEntity.ok().body(storeDetailResponse);
        return response;
    }
}
