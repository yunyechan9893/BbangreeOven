package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.SearchResponseDto;
import com.bbangle.bbangle.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponseDto> getSearchedBoard(
            @RequestParam
            String keyword
    ){
        return ResponseEntity.ok().body(searchService.getSearchResult(keyword));
    }
}
