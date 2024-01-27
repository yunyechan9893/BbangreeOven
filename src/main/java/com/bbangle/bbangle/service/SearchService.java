package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.KeywordDto;
import com.bbangle.bbangle.dto.SearchResponseDto;

import java.util.List;

public interface SearchService {

    SearchResponseDto getSearchResult(String keyword);

    List<KeywordDto> getRecencyKeyword(Long accessToken);

    Boolean deleteRecencyKeyword(Long keywordId);

    void updateRedisAtBestKeyword();

    List<String> getBestKeyword();
}
