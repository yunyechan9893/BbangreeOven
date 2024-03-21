package com.bbangle.bbangle.search.service;

import com.bbangle.bbangle.search.dto.RecencySearchResponse;
import com.bbangle.bbangle.search.dto.SearchBoardDto;
import com.bbangle.bbangle.search.dto.SearchStoreDto;

import java.util.List;

public interface SearchService {

    void initSetting();

    void updateRedisAtBestKeyword();
    void saveKeyword(Long memberId,String keyword);

    SearchBoardDto getSearchBoardDtos(Long memberId, int boardPage, String keyword, String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                      Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                      Boolean orderAvailableToday, String category, Integer minPrice, Integer maxPrice);

    SearchStoreDto getSearchStoreDtos(Long memberId, int storePage, String keyword);

    RecencySearchResponse getRecencyKeyword(Long memberId);

    Boolean deleteRecencyKeyword(String keyword, Long memberId);

    List<String> getBestKeyword();

    List<String> getAutoKeyword(String keyword);
}
