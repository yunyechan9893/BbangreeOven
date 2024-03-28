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

    public static SearchStoreDto getEmpty(int pageNumber, int limitItemCount){
        return SearchStoreDto.builder().content(List.of())
                .itemAllCount(0)
                .pageNumber(pageNumber)
                .limitItemCount(limitItemCount)
                .currentItemCount(0)
                .existNextPage(false)
                .build();
    }
}
