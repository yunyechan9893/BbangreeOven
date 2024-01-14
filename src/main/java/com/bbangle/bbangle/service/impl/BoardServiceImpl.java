package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.repository.BoardRepository;
import com.bbangle.bbangle.repository.impl.BoardRepositoryImpl;
import com.bbangle.bbangle.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    public List<BoardResponseDto> getBoardList(String sort, Boolean glutenFreeTag, Boolean highProteinTag, Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag, String category) {
        return boardRepository.getBoardResponseDto(
                sort,
                glutenFreeTag,
                highProteinTag,
                sugarFreeTag,
                veganTag,
                ketogenicTag,
                category
        );
    }

    @Override
    public BoardDetailResponseDto getBoardDetailResponse(Long boardId) {
        return boardRepository.getBoardDetailResponseDto(boardId);
    }
}
