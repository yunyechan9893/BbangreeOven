package com.bbangle.bbangle.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.exception.MemberNotFoundException;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.model.SortType;
import com.bbangle.bbangle.model.WishlistFolder;
import com.bbangle.bbangle.repository.BoardRepository;
import com.bbangle.bbangle.repository.MemberRepository;
import com.bbangle.bbangle.repository.WishListFolderRepository;
import com.bbangle.bbangle.service.BoardService;
import com.bbangle.bbangle.util.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WishListFolderRepository folderRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(readOnly = true)
    public Slice<BoardResponseDto> getBoardList(String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                                Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                                String category, Integer minPrice, Integer maxPrice,
                                                Pageable pageable) {

        List<BoardResponseDto> boardResponseDto = boardRepository.getBoardResponseDto(
            sort,
            glutenFreeTag,
            highProteinTag,
            sugarFreeTag,
            veganTag,
            ketogenicTag,
            category,
            minPrice,
            maxPrice
        );

        List<Long> boardResponseDtoIdx = boardResponseDto.stream()
            .map(BoardResponseDto::boardId)
            .toList();

        List<Long> matchedIdx = getListAdaptingSort(boardResponseDtoIdx, sort);

        List<BoardResponseDto> sortedBoardResponseDto = boardResponseDto.stream()
            .sorted(Comparator.comparingInt(dto -> matchedIdx.indexOf(dto.boardId()))) // matchedIdx의 순서에 따라 정렬
            .toList();

        // 현재 페이지와 페이지 크기 계산
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int startItem = currentPage * pageSize;
        boolean hasNext = false;

        List<BoardResponseDto> pageContent;

        // 현재 페이지 데이터 추출
        if (sortedBoardResponseDto.size() > startItem) {
            int toIndex = Math.min(startItem + pageSize, sortedBoardResponseDto.size());
            pageContent = new ArrayList<>(sortedBoardResponseDto.subList(startItem, toIndex));
            hasNext = sortedBoardResponseDto.size() > toIndex;
        } else {
            pageContent = Collections.emptyList();
        }

        return new SliceImpl<>(pageContent, pageable, hasNext);
    }

    private List<Long> getListAdaptingSort(List<Long> boardResponseDtoIdx, String sort) {
        if (sort != null && sort.equals(SortType.POPULAR.getValue())) {
            return redisTemplate.opsForZSet().reverseRange(RedisKeyUtil.POPULAR_KEY, 0, -1)
                .stream()
                .map(idx -> Long.valueOf(String.valueOf(idx)))
                .filter(boardResponseDtoIdx::contains)
                .toList();
        }
        return redisTemplate.opsForZSet().reverseRange(RedisKeyUtil.RECOMMEND_KEY, 0, -1)
            .stream()
            .map(idx -> Long.valueOf(String.valueOf(idx)))
            .filter(boardResponseDtoIdx::contains)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoardDetailResponse(Long boardId) {
        return boardRepository.getBoardDetailResponseDto(boardId);
    }

    public Slice<BoardResponseDto> getPostInFolder(Long memberId, String sort, Long folderId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        WishlistFolder folder = folderRepository.findByMemberAndId(member, folderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 폴더입니다."));
        if (folder.isDeleted()) {
            throw new IllegalArgumentException("존재하지 않는 폴더입니다.");
        }

        return boardRepository.getAllByFolder(sort, pageable, folderId, folder);
    }


}
