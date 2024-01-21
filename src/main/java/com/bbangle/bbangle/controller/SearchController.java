package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping
    public ResponseEntity<Slice<BoardResponseDto>> getSearchedBoard(
            @RequestParam
            String title
    ){
        return ResponseEntity.ok().body(searchService.getBoardIdes(title));
    }
}
