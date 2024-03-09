package com.bbangle.bbangle.dto;

import org.springframework.web.multipart.MultipartFile;

public record AdminBoardRequestDto(
        String title,
        Integer price,
        Boolean status,
        String purchaseUrl,
        String detailUrl,
        Boolean mon,
        Boolean tue,
        Boolean wed,
        Boolean thr,
        Boolean fri,
        Boolean sat,
        Boolean sun
) {
}
