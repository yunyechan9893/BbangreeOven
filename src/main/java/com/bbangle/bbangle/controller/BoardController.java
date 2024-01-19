package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.service.impl.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardServiceImpl boardService;

    @GetMapping("")
    public ResponseEntity<Slice<BoardResponseDto>> getList(
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean glutenFreeTag,
        @RequestParam(required = false) Boolean highProteinTag,
        @RequestParam(required = false) Boolean sugarFreeTag,
        @RequestParam(required = false) Boolean veganTag,
        @RequestParam(required = false) Boolean ketogenicTag,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Integer minPrice,
        @RequestParam(required = false) Integer maxPrice,
        @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.getBoardList(sort,
            glutenFreeTag,
            highProteinTag,
            sugarFreeTag,
            veganTag,
            ketogenicTag,
            category,
            minPrice,
            maxPrice,
            pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDetailResponseDto> getBoardDetailResponse(
        @PathVariable("id")
        Long boardId
    ) {
        return ResponseEntity.ok().body(
            boardService.getBoardDetailResponse(boardId)
        );
    }

}

