package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.KeywordDto;
import com.bbangle.bbangle.dto.SearchBoardDto;
import com.bbangle.bbangle.dto.StoreResponseDto;
import com.bbangle.bbangle.member.domain.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchQueryDSLRepository {

    SearchBoardDto getSearchResult(Long memberId, List<Long> boardIds, String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                   Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag, Boolean orderAvailableToday,
                                   String category, Integer minPrice, Integer maxPrice, Pageable pageable);
    List<StoreResponseDto> getSearchedStore(Long memberId, List<Long> storeIndexList, Pageable pageable);

    List<KeywordDto> getRecencyKeyword(Member member);

    String[] getBestKeyword();

    void markAsDeleted(String keyword, Member member);

}
