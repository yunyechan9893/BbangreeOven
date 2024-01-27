package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.SearchResponseDto;

public interface SearchService {

    SearchResponseDto getSearchResult(String keyword);

}
