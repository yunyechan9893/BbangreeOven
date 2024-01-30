package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.KeywordDto;
import com.bbangle.bbangle.dto.SearchResponseDto;

import java.util.List;

public interface SearchService {

    void loadData();

    void updateRedisAtBestKeyword();

    SearchResponseDto getSearchResult(String keyword);
    void saveKeyword(Long memberId,String keyword);

    List<KeywordDto> getRecencyKeyword(Long accessToken);

    Boolean deleteRecencyKeyword(Long keywordId);

    List<String> getBestKeyword();

    List<String> getAutoKeyword(String keyword);
}
