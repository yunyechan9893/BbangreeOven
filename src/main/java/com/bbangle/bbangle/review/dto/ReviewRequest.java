package com.bbangle.bbangle.review.dto;

import com.bbangle.bbangle.common.domain.Badge;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public record ReviewRequest(
    List<Badge> badges,
    BigDecimal rate,
    String content,
    Long boardId,
    List<MultipartFile> photos
) {
}
