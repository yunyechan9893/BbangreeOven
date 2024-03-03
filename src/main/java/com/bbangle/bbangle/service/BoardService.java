package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    Slice<BoardResponseDto> getBoardList(String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                         Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                         String category, Integer minPrice, Integer maxPrice, Pageable pageable);
    @Transactional(readOnly = true)
    BoardDetailResponseDto getBoardDetailResponse(Long memberId, Long boardId);

    Boolean saveBoardDetailHtml(Long boardId, MultipartFile htmlFile);
}
