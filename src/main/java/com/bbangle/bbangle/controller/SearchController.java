package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.KeywordDto;
import com.bbangle.bbangle.dto.MessageResDto;
import com.bbangle.bbangle.dto.SearchResponseDto;
import com.bbangle.bbangle.service.SearchService;
import com.bbangle.bbangle.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/search")
public class SearchController {
    private final Long ANONYMOUS_ID = 1L;

    @Autowired
    SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponseDto> getSearchedBoard(
            @RequestParam(value = "keyword")
            String keyword
    ){
        return ResponseEntity.ok().body(searchService.getSearchResult(keyword));
    }

    @PostMapping
    public ResponseEntity<MessageResDto> saveKeyword(
            @RequestParam
            String keyword
    ){
        Long memberId = SecurityUtils.getUserIdWithAnonymous();

        // 유저가 아니라면 비회원 아이디를 사용
        if (memberId == null){
            memberId = ANONYMOUS_ID;
        }

        try {
            searchService.saveKeyword(memberId, keyword);
            return ResponseEntity.ok().body(new MessageResDto("검색어 저장 완료"));
        } catch (Exception e){
            return ResponseEntity.ok().body(new MessageResDto("검색어 저장 실패"));
        }
    }

    @GetMapping("/recency")
    public ResponseEntity<List<KeywordDto>> getRecencyKeyword(){

        // 임의로 구성
        Long memberId = SecurityUtils.getMemberId();
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

    @GetMapping("/best-keyword")
    public ResponseEntity<List<String>> getBestKeyword(){
        return ResponseEntity.ok().body(
            searchService.getBestKeyword()
        );
    }

    @GetMapping("/auto-keyword")
    public ResponseEntity<List<String>> getAutoKeyword(
            @RequestParam
            String keyword
    ){
        return ResponseEntity.ok().body(
                searchService.getAutoKeyword(keyword)
        );
    }
}
