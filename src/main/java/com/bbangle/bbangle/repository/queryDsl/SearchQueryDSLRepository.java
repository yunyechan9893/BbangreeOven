package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.dto.KeywordDto;
import com.bbangle.bbangle.dto.StoreResponseDto;
import com.bbangle.bbangle.model.Member;

import java.util.List;

public interface SearchQueryDSLRepository {
    List<BoardResponseDto> getSearchResult(List<Long> boardIds,String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                           Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                           String category, Integer minPrice, Integer maxPrice);

    List<BoardResponseDto> getSearchResultWithLike(Long memberId, List<Long> boardIds, String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                                   Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                                   String category, Integer minPrice, Integer maxPrice);

    List<StoreResponseDto> getSearchedStore(List<Long> storeIndexList);

    List<StoreResponseDto> getSearchedStoreWithLike(Long memberId, List<Long> storeIndexList);

    List<KeywordDto> getRecencyKeyword(Member member);
    String[] getBestKeyword();
    void markAsDeleted(String keyword, Member member);
}
