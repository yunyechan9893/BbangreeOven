package com.bbangle.bbangle.admin.controller;

import com.bbangle.bbangle.admin.dto.AdminBoardImgResponseDto;
import com.bbangle.bbangle.admin.dto.AdminBoardRequestDto;
import com.bbangle.bbangle.admin.dto.AdminBoardResponseDto;
import com.bbangle.bbangle.admin.dto.AdminProductRequestDto;
import com.bbangle.bbangle.admin.dto.AdminStoreRequestDto;
import com.bbangle.bbangle.admin.dto.AdminStoreResponseDto;
import com.bbangle.bbangle.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping(value = "/store", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<AdminStoreResponseDto> uploadStore(
        @ModelAttribute
        AdminStoreRequestDto adminStoreRequestDto,
        @RequestPart("profile")
        MultipartFile profile
    ) {
        return ResponseEntity.ok()
            .body(
                AdminStoreResponseDto.builder()
                    .storeId(adminService.uploadStore(adminStoreRequestDto, profile))
                    .build()
            );
    }

    @PostMapping(
        value = "/stores/{storeId}/board", consumes = {"multipart/form-data", "application/json"}
    )
    public ResponseEntity<AdminBoardResponseDto> uploadBoard(
        @PathVariable("storeId")
        Long storeId,
        @ModelAttribute
        AdminBoardRequestDto adminBoardRequestDto,
        @RequestPart("profile")
        MultipartFile profile
    ) {
        return ResponseEntity.ok()
            .body(
                AdminBoardResponseDto.builder()
                    .boardId(adminService.uploadBoard(profile, storeId, adminBoardRequestDto))
                    .build()
            );
    }

    @PostMapping(
        value = "/stores/{storeId}/boards/{boardId}/subimage",
        consumes = {"multipart/form-data", "application/json"}
    )
    public ResponseEntity<AdminBoardImgResponseDto> uploadBoardImg(
        @PathVariable("storeId")
        Long storeId,
        @PathVariable("boardId")
        Long boardId,
        @RequestPart("subimage")
        MultipartFile subImage
    ) {
        return ResponseEntity.ok()
            .body(
                AdminBoardImgResponseDto.builder()
                    .message(adminService.uploadBoardImage(storeId, boardId, subImage) ? "저장 성공"
                        : "저장 실패")
                    .build()
            );
    }

    @PostMapping(value = "/stores/{storeId}/boards/{boardId}/product")
    public ResponseEntity<AdminBoardImgResponseDto> uploadProduct(
        @PathVariable("storeId")
        Long storeId,
        @PathVariable("boardId")
        Long boardId,
        @RequestBody
        AdminProductRequestDto adminProductRequestDto
    ) {
        adminService.uploadProduct(storeId, boardId, adminProductRequestDto);
        return ResponseEntity.ok()
            .body(
                AdminBoardImgResponseDto.builder()
                    .message("저장 성공")
                    .build()
            );
    }

}
