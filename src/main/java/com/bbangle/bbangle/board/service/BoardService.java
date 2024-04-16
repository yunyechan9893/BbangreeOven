package com.bbangle.bbangle.board.service;


import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_MEMBER;

import com.bbangle.bbangle.aop.ExecutionTimeLog;
import com.bbangle.bbangle.board.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.board.dto.BoardResponseDto;
import com.bbangle.bbangle.board.repository.BoardRepository;
import com.bbangle.bbangle.common.image.repository.ObjectStorageRepository;
import com.bbangle.bbangle.common.sort.SortType;
import com.bbangle.bbangle.config.ranking.BoardLikeInfo;
import com.bbangle.bbangle.config.ranking.ScoreType;
import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.board.dto.FilterRequest;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.page.BoardCustomPage;
import com.bbangle.bbangle.ranking.domain.Ranking;
import com.bbangle.bbangle.ranking.repository.RankingRepository;
import com.bbangle.bbangle.store.repository.StoreRepository;
import com.bbangle.bbangle.util.RedisKeyUtil;
import com.bbangle.bbangle.util.SecurityUtils;
import com.bbangle.bbangle.wishListFolder.domain.WishlistFolder;
import com.bbangle.bbangle.wishListFolder.repository.WishListFolderRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardServiceImpl implements BoardService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WishListFolderRepository folderRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> boardLikeInfoRedisTemplate;
    private final RankingRepository rankingRepository;
    private final ObjectStorageRepository objectStorageRepository;
    private final StoreRepository storeRepository;

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
        RedisTemplate<String, Object> redisTemplate,
        @Autowired
        @Qualifier("boardLikeInfoRedisTemplate")
        RedisTemplate<String, Object> boardLikeInfoRedisTemplate,
        RankingRepository rankingRepository,
        ObjectStorageRepository objectStorageRepository,
        @Autowired
        StoreRepository storeRepository
    ) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.folderRepository = folderRepository;
        this.redisTemplate = redisTemplate;
        this.boardLikeInfoRedisTemplate = boardLikeInfoRedisTemplate;
        this.rankingRepository = rankingRepository;
        this.objectStorageRepository = objectStorageRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public BoardCustomPage<List<BoardResponseDto>> getBoardList(
        FilterRequest filterRequest,
        SortType sort,
        Long cursorId
    ) {
        validateCursorId(cursorId);
        if(SecurityUtils.isLogin()){
            return boardRepository.getBoardResponseWithLogin(filterRequest, sort, cursorId);
        }

        return boardRepository.getBoardResponseDtoWithoutLogin(
            filterRequest,
            sort,
            cursorId
        );
    }

    private void validateCursorId(Long cursorId) {
        if(Objects.nonNull(cursorId) && !boardRepository.existsById(cursorId)){
            throw new BbangleException(BbangleErrorCode.BOARD_NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoardDetailResponse(Long memberId, Long boardId) {

        return boardRepository.getBoardDetailResponse(memberId, boardId);
    }

    @Override
    @Transactional
    public Boolean saveBoardDetailHtml(Long boardId, MultipartFile htmlFile) {
//        Long storeId = boardRepository.findById(boardId)
//            .get()
//            .getStore()
//            .getId();
//        String filePath = String.format("%s/%s/%s", storeId, boardId, DETAIL_HTML_FILE_NAME);
//        // Board DetailUrl FilePath로 수정
//        if (boardRepository.updateDetailWhereStoreIdEqualsBoardId(
//            boardId,
//            filePath
//        ) != 1) {
//            return false;
//        }

        // ObjectStorage에 파일 생성
//        return objectStorageRepository.createFile(BUCKET_NAME, filePath, htmlFile);
        return null;
    }

    public Slice<BoardResponseDto> getPostInFolder(
        Long memberId,
        String sort,
        Long folderId,
        Pageable pageable
    ) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));

        WishlistFolder folder = folderRepository.findByMemberAndId(member, folderId)
            .orElseThrow(() -> new BbangleException("존재하지 않는 폴더입니다."));

        return boardRepository.getAllByFolder(sort, pageable, folderId, folder);
    }

    @ExecutionTimeLog
    public List<Long> getListAdaptingSort(
        SortType sort
    ) {
        if (sort != null && sort.equals(SortType.POPULAR)) {
            return getPopularIdList();
        }

        return getRecommentIdList();
    }

    private List<Long> getRecommentIdList() {
        return Objects.requireNonNull(redisTemplate.opsForZSet()
                .reverseRange(RedisKeyUtil.RECOMMEND_KEY, 0, -1))
            .stream()
            .map(value -> Long.valueOf(String.valueOf(value)))
            .toList();
    }

    private List<Long> getPopularIdList() {
        return Objects.requireNonNull(redisTemplate.opsForZSet()
                .reverseRange(RedisKeyUtil.POPULAR_KEY, 0, -1))
            .stream()
            .map(value -> Long.valueOf(String.valueOf(value)))
            .toList();
    }

    @Transactional
    public void updateCountView(Long boardId, String viewCountKey) {
        Ranking ranking = rankingRepository.findByBoardId(boardId)
            .orElseThrow(() -> new BbangleException(BbangleErrorCode.RANKING_NOT_FOUND));
        ranking.updatePopularScore(0.1);

        boardLikeInfoRedisTemplate.opsForList()
            .rightPush(
                LocalDateTime.now().format(formatter),
                new BoardLikeInfo(boardId, 0.1, LocalDateTime.now(), ScoreType.VIEW));

        redisTemplate.opsForValue()
            .set(viewCountKey, "true", Duration.ofMinutes(3));
    }

    @Transactional
    public void adaptPurchaseReaction(Long boardId, String purchaseCountKey) {
        Ranking ranking = rankingRepository.findByBoardId(boardId)
            .orElseThrow(() -> new BbangleException(BbangleErrorCode.RANKING_NOT_FOUND));
        ranking.updatePopularScore(1.0);

        boardLikeInfoRedisTemplate.opsForList()
            .rightPush(LocalDateTime.now()
                    .format(formatter),
                new BoardLikeInfo(boardId, 1, LocalDateTime.now(), ScoreType.PURCHASE));

        redisTemplate.opsForValue()
            .set(purchaseCountKey, "true", Duration.ofMinutes(3));
    }

}
