package com.bbangle.bbangle.admin.service;

import com.bbangle.bbangle.admin.dto.AdminBoardRequestDto;
import com.bbangle.bbangle.admin.dto.AdminProductRequestDto;
import com.bbangle.bbangle.admin.dto.AdminStoreRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    Long uploadStore(AdminStoreRequestDto adminStoreRequestDto, MultipartFile profile);
    Long uploadBoard(MultipartFile profile, Long storeId, AdminBoardRequestDto adminBoardRequestDto);
    Boolean uploadBoardImage(Long storeId, Long boardId, MultipartFile profile);

    void uploadProduct(Long storeId, Long boardId, AdminProductRequestDto adminProductRequestDto);
}
