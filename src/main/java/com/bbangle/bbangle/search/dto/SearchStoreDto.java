package com.bbangle.bbangle.search.dto;

import com.bbangle.bbangle.store.dto.StoreResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record SearchStoreDto(
        List<StoreResponseDto> content,
        int pageNumber,
        int itemAllCount,
        int limitItemCount,
        int currentItemCount,
        boolean existNextPage
){
}
