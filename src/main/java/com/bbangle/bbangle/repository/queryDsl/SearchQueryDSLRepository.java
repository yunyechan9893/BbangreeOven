package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.dto.KeywordDto;
import com.bbangle.bbangle.dto.StoreResponseDto;
import com.bbangle.bbangle.model.Member;

import java.util.List;

public interface SearchQueryDSLRepository {
    List<BoardResponseDto> getSearchResult(List<Long> boardIdes);
    List<StoreResponseDto> getSearchedStore(List<Long> storeIndexList);
    List<KeywordDto> getRecencyKeyword(Member member);
    String[] getBestKeyword();
    void markAsDeleted(String keyword, Member member);
    void getTest();
}
