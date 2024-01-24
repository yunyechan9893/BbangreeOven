package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.dto.BoardResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface SearchRepository {
    Boolean saveSearchKeyword(Long id, String keyword);
    Slice<BoardResponseDto> getSearchResult(List<Long> boardIdes, Pageable pageable);
}
