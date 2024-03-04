package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.WishProductRequestDto;
import com.bbangle.bbangle.service.WishListProductService;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards/{boardId}/wish")
@RequiredArgsConstructor
public class WishListProductController {

    private final WishListProductService wishListProductService;

    @PatchMapping
    public ResponseEntity<Void> wish(
        @PathVariable
        Long boardId,
        @RequestBody
        WishProductRequestDto wishRequest
    ) {
        Long memberId = SecurityUtils.getMemberId();
        wishListProductService.wish(memberId, boardId, wishRequest);

        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

}
