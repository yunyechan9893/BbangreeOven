package com.bbangle.bbangle.board.service;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.Product;
import com.bbangle.bbangle.board.domain.TagEnum;
import com.bbangle.bbangle.board.dto.BoardDetailResponse;
import com.bbangle.bbangle.board.dto.BoardResponseDto;
import com.bbangle.bbangle.board.dto.CursorInfo;
import com.bbangle.bbangle.board.dto.FilterRequest;
import com.bbangle.bbangle.board.repository.BoardRepository;
import com.bbangle.bbangle.common.sort.SortType;
import com.bbangle.bbangle.config.ranking.BoardLikeInfo;
import com.bbangle.bbangle.config.ranking.ScoreType;
import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.page.BoardCustomPage;
import com.bbangle.bbangle.ranking.domain.Ranking;
import com.bbangle.bbangle.ranking.repository.RankingRepository;
import com.bbangle.bbangle.wishlist.domain.WishListFolder;
import com.bbangle.bbangle.wishlist.repository.WishListFolderRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WishListFolderRepository folderRepository;
    @Qualifier("defaultRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    @Qualifier("boardLikeInfoRedisTemplate")
    private final RedisTemplate<String, Object> boardLikeInfoRedisTemplate;
    private final RankingRepository rankingRepository;

    @Transactional(readOnly = true)
    public BoardCustomPage<List<BoardResponseDto>> getBoardList(
        FilterRequest filterRequest,
        SortType sort,
        CursorInfo cursorInfo,
        Long memberId
    ) {
        BoardCustomPage<List<BoardResponseDto>> boards = boardRepository
            .getBoardResponseList(filterRequest, sort, cursorInfo);

        if (Objects.nonNull(memberId) && memberRepository.existsById(memberId)) {
            updateLikeStatus(boards, memberId);
        }

        return boards;
    }

    private void updateLikeStatus(
        BoardCustomPage<List<BoardResponseDto>> boardResponseDto,
        Long memberId
    ) {
        List<Long> responseList = extractIds(boardResponseDto);
        List<Long> likedContentIds = boardRepository.getLikedContentsIds(responseList, memberId);

        boardResponseDto.getContent()
            .stream()
            .filter(board -> likedContentIds.contains(board.getBoardId()))
            .forEach(board -> board.updateLike(true));
    }

    private List<Long> extractIds(
        BoardCustomPage<List<BoardResponseDto>> boardResponseDto
    ) {
        return boardResponseDto.getContent()
            .stream()
            .map(BoardResponseDto::getBoardId)
            .toList();
    }

    @Transactional(readOnly = true)
    public BoardDetailResponse getBoardDetailResponse(Long memberId, Long boardId) {
        return boardRepository.getBoardDetailResponse(memberId, boardId);
    }

    @Transactional(readOnly = true)
    public BoardCustomPage<List<BoardResponseDto>> getPostInFolder(
        Long memberId,
        SortType sort,
        Long folderId,
        Long cursorId
    ) {
        WishListFolder folder = folderRepository.findByMemberIdAndId(memberId, folderId)
            .orElseThrow(() -> new BbangleException(BbangleErrorCode.FOLDER_NOT_FOUND));

        List<Board> allByFolder = boardRepository.getAllByFolder(sort, cursorId, folder, memberId);

        if (allByFolder.isEmpty()){
            return BoardCustomPage.emptyPage();
        }
        return getListBoardCustomPage(allByFolder);
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

    private BoardCustomPage<List<BoardResponseDto>> getListBoardCustomPage(List<Board> allByFolder) {
        List<BoardResponseDto> boardResponseDtos = convertToBoardResponse(allByFolder);
        Long nextCursor = allByFolder.get(allByFolder.size()-1).getId();
        boolean hasNext = false;
        if(allByFolder.size() == 11){
            hasNext = true;
        }
        return BoardCustomPage.from(boardResponseDtos,
            nextCursor, hasNext);
    }

    private List<BoardResponseDto> convertToBoardResponse(List<Board> boards) {
        Map<Long, List<String>> tagMapByBoardId = boards.stream()
            .collect(Collectors.toMap(
                Board::getId,
                board -> extractTags(board.getProductList())
            ));

        return boards.stream()
            .limit(10)
            .map(board -> BoardResponseDto.from(board, tagMapByBoardId.get(board.getId())))
            .toList();
    }

    private List<String> extractTags(List<Product> products) {
        if (Objects.isNull(products)) {
            return Collections.emptyList();
        }

        HashSet<String> tags = new HashSet<>();
        for (Product dto : products) {
            addTagIfTrue(tags, dto.isGlutenFreeTag(), TagEnum.GLUTEN_FREE.label());
            addTagIfTrue(tags, dto.isHighProteinTag(), TagEnum.HIGH_PROTEIN.label());
            addTagIfTrue(tags, dto.isSugarFreeTag(), TagEnum.SUGAR_FREE.label());
            addTagIfTrue(tags, dto.isVeganTag(), TagEnum.VEGAN.label());
            addTagIfTrue(tags, dto.isKetogenicTag(), TagEnum.KETOGENIC.label());
        }
        return new ArrayList<>(tags);
    }

    private void addTagIfTrue(Set<String> tags, boolean condition, String tag) {
        if (condition) {
            tags.add(tag);
        }
    }
}
