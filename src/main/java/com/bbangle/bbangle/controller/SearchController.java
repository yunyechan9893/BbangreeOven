package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.KeywordDto;
import com.bbangle.bbangle.dto.SearchResponseDto;
import com.bbangle.bbangle.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/recency")
    public ResponseEntity<List<KeywordDto>> getRecencyKeyword(){

        // 임의로 구성
        Long memberId = 1L;

        List<KeywordDto> keywords = searchService.getRecencyKeyword(memberId);

        var successResponse = ResponseEntity.ok().body(keywords);

        return successResponse;
    }

    @DeleteMapping("/recency/{keywordId}")
    public ResponseEntity<Boolean> deleteRecencyKeyword(
            @PathVariable
            Long keywordId){
        Boolean isDeleted = searchService.deleteRecencyKeyword(keywordId);

        return ResponseEntity.ok().body(isDeleted);
    }
}
