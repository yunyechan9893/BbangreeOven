package com.bbangle.bbangle.board.service;


import com.bbangle.bbangle.board.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.board.dto.BoardResponseDto;
import com.bbangle.bbangle.exception.MemberNotFoundException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.common.sort.SortType;
import com.bbangle.bbangle.wishListFolder.domain.WishlistFolder;
import com.bbangle.bbangle.board.repository.BoardRepository;
import com.bbangle.bbangle.common.image.repository.ObjectStorageRepository;
import com.bbangle.bbangle.wishListFolder.repository.WishListFolderRepository;
import com.bbangle.bbangle.util.RedisKeyUtil;
import com.bbangle.bbangle.util.SecurityUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WishListFolderRepository folderRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectStorageRepository objectStorageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;
    private final String DETAIL_HTML_FILE_NAME = "detail.html";

    public BoardServiceImpl(
        @Autowired
        BoardRepository boardRepository,
        @Autowired
        MemberRepository memberRepository,
        @Autowired
        WishListFolderRepository folderRepository,
        @Autowired
        @Qualifier("defaultRedisTemplate")
        RedisTemplate<String, Object> redisTemplate, ObjectStorageRepository objectStorageRepository
    ) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.folderRepository = folderRepository;
        this.redisTemplate = redisTemplate;
        this.objectStorageRepository = objectStorageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<BoardResponseDto> getBoardList(
        String sort, Boolean glutenFreeTag, Boolean highProteinTag,
        Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
        String category, Integer minPrice, Integer maxPrice, Boolean orderAvailableToday,
        Pageable pageable
        ) {

        List<BoardResponseDto> boardResponseDto = boardRepository.getBoardResponseDto(
            sort,
            glutenFreeTag,
            highProteinTag,
            sugarFreeTag,
            veganTag,
            ketogenicTag,
            category,
            minPrice,
            maxPrice,
            orderAvailableToday
        );

        List<Long> boardResponseDtoIdx = boardResponseDto.stream()
            .map(BoardResponseDto::boardId)
            .toList();

        List<Long> matchedIdx = getListAdaptingSort(boardResponseDtoIdx, sort);

        List<BoardResponseDto> sortedBoardResponseDto = boardResponseDto.stream()
            .sorted(Comparator.comparingInt(
                dto -> matchedIdx.indexOf(dto.boardId()))) // matchedIdx의 순서에 따라 정렬
            .toList();

        if (SecurityUtils.isLogin()) {
            sortedBoardResponseDto = boardRepository.updateLikeStatus(matchedIdx, sortedBoardResponseDto);
        }

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
            return redisTemplate.opsForZSet()
                .reverseRange(RedisKeyUtil.POPULAR_KEY, 0, -1)
                .stream()
                .map(idx -> Long.valueOf(idx.toString()
                    .replace("\"", "")))
                .filter(boardResponseDtoIdx::contains)
                .toList();
        }
        return redisTemplate.opsForZSet()
            .reverseRange(RedisKeyUtil.RECOMMEND_KEY, 0, -1)
            .stream()
            .map(idx -> Long.valueOf(idx.toString()
                .replace("\"", "")))
            .filter(boardResponseDtoIdx::contains)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoardDetailResponse(Long memberId, Long boardId) {

        return memberId > 1L ?
                boardRepository.getBoardDetailResponseDtoWithLike(memberId, boardId) :
                boardRepository.getBoardDetailResponseDto(boardId);

    }

    @Override
    @Transactional
    public Boolean saveBoardDetailHtml(Long boardId, MultipartFile htmlFile) {
        Long storeId = boardRepository.findById(boardId)
            .get()
            .getStore()
            .getId();
        String filePath = String.format("%s/%s/%s", storeId, boardId, DETAIL_HTML_FILE_NAME);
        // Board DetailUrl FilePath로 수정
        if (boardRepository.updateDetailWhereStoreIdEqualsBoardId(
            boardId,
            filePath
        ) != 1) {
            return false;
        }

        // ObjectStorage에 파일 생성
        return objectStorageRepository.createFile(BUCKET_NAME, filePath, htmlFile);
    }

    public Slice<BoardResponseDto> getPostInFolder(
        Long memberId,
        String sort,
        Long folderId,
        Pageable pageable
    ) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        WishlistFolder folder = folderRepository.findByMemberAndId(member, folderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 폴더입니다."));

        return boardRepository.getAllByFolder(sort, pageable, folderId, folder);
    }


}
