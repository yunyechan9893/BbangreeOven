package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.service.impl.BoardServiceImpl;
import com.bbangle.bbangle.util.RedisKeyUtil;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardServiceImpl boardService;
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping
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

    @GetMapping("/folders/{folderId}")
    public ResponseEntity<Slice<BoardResponseDto>> getPostInFolder(@RequestParam(required = false) String sort,
                                                                   @PathVariable Long folderId,
                                                                   @PageableDefault Pageable pageable){
        Long memberId = SecurityUtils.getMemberId();
        return ResponseEntity.ok(boardService.getPostInFolder(memberId, sort, folderId, pageable));
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

    @PatchMapping("/{boardId}")
    public ResponseEntity<Void> countView(@PathVariable Long boardId){
        redisTemplate.opsForZSet().incrementScore(RedisKeyUtil.POPULAR_KEY, boardId, 0.1);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{boardId}/purchase")
    public ResponseEntity<Void> movePurchasePage(@PathVariable Long boardId){
        redisTemplate.opsForZSet().incrementScore(RedisKeyUtil.POPULAR_KEY, boardId, 1);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

