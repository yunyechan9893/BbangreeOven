package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.BoardResponseDto;
import org.springframework.data.domain.Slice;

public interface SearchService {

    Slice<BoardResponseDto> getBoardIdes(String title);

}
