package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<BoardResponseDto> getBoardList(String sort) {

        if (sort == null || sort.isEmpty()){
            return boardRepository.getBoardResponseDto();
        }
        return null;
    }

}
