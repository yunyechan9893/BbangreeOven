package com.bbangle.bbangle.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record SearchStoreDto(
    List<StoreResponseDto> content,
    int itemCount,
    int pageNumber,
    int pageSize
) {

}
