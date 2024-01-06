package com.bbangle.bbangle.controller;

import java.util.List;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("")
    public ResponseEntity<List<BoardResponseDto>> getList(@RequestParam(required = false) String sort){
        return ResponseEntity.ok(boardService.getBoardList(sort));
    }
}
