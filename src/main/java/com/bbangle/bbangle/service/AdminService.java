package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.*;
import com.bbangle.bbangle.model.Category;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    Long uploadStore(AdminStoreRequestDto adminStoreRequestDto, MultipartFile profile);
    Long uploadBoard(MultipartFile profile, Long storeId, AdminBoardRequestDto adminBoardRequestDto);
    Boolean uploadBoardImage(Long storeId, Long boardId, MultipartFile profile);

    void uploadProduct(Long storeId, Long boardId, AdminProductRequestDto adminProductRequestDto);
}
