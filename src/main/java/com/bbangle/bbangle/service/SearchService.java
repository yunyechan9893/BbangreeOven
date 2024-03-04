package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.RecencySearchResponse;
import com.bbangle.bbangle.dto.SearchResponseDto;
import java.util.List;

public interface SearchService {

    void initSetting();

    void updateRedisAtBestKeyword();

    SearchResponseDto getSearchResult(
        int storePage,
        int boardPage,
        String keyword,
        String sort,
        Boolean glutenFreeTag,
        Boolean highProteinTag,
        Boolean sugarFreeTag,
        Boolean veganTag,
        Boolean ketogenicTag,
        String category,
        Integer minPrice,
        Integer maxPrice
    );

    void saveKeyword(Long memberId, String keyword);

    RecencySearchResponse getRecencyKeyword(Long memberId);

    Boolean deleteRecencyKeyword(String keyword, Long memberId);

    List<String> getBestKeyword();

    List<String> getAutoKeyword(String keyword);

}
