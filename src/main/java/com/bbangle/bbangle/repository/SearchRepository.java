package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.dto.BoardResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.HashMap;
import java.util.List;

public interface SearchRepository {

    Slice<BoardResponseDto> getSearchBoardResult(List<Long> boardIdes, Pageable pageable);
}
