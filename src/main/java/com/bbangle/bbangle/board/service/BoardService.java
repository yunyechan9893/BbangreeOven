package com.bbangle.bbangle.board.service;


import static com.bbangle.bbangle.exception.BbangleErrorCode.BOARD_NOT_FOUND;
import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_MEMBER;

import com.bbangle.bbangle.board.dto.BoardDetailProductDto;
import com.bbangle.bbangle.board.dto.BoardDetailResponse;
import com.bbangle.bbangle.board.dto.BoardResponse;
import com.bbangle.bbangle.board.dto.BoardResponseDto;
import com.bbangle.bbangle.board.dto.CursorInfo;
import com.bbangle.bbangle.board.dto.FilterRequest;
import com.bbangle.bbangle.board.dto.ProductDto;
import com.bbangle.bbangle.board.dto.ProductResponse;
import com.bbangle.bbangle.board.dto.StoreAndBoardImgResponse;
import com.bbangle.bbangle.board.dto.TagDto;
import com.bbangle.bbangle.board.repository.BoardRepository;
import com.bbangle.bbangle.common.sort.SortType;
import com.bbangle.bbangle.config.ranking.BoardLikeInfo;
import com.bbangle.bbangle.config.ranking.ScoreType;
import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.page.BoardCustomPage;
import com.bbangle.bbangle.ranking.domain.Ranking;
import com.bbangle.bbangle.ranking.repository.RankingRepository;
import com.bbangle.bbangle.wishlist.domain.WishListFolder;
import com.bbangle.bbangle.wishlist.repository.WishListFolderRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.datafaker.providers.base.Bool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    private final int ONE_CATEGOTY = 1;

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

    public StoreAndBoardImgResponse getStoreAndBoardResponse(Long memberId, Long boardId){
        return boardRepository.getStoreAndBoardImgResponse(memberId, boardId);
    }

    public BoardResponse getBoardDetailResponse(Long memberId, Long boardId){
        return boardRepository.getBoardDetailResponse(memberId, boardId);
    }

    private List<BoardDetailProductDto> toProductToResponse(List<ProductDto> productDtos){
        return productDtos.stream().map(product -> BoardDetailProductDto.builder()
            .productId(product.productId())
            .productTitle(product.productTitle())
            .gluten_free_tag(product.gluten_free_tag())
            .sugar_free_tag(product.sugar_free_tag())
            .high_protein_tag(product.high_protein_tag())
            .vegan_tag(product.vegan_tag())
            .ketogenic_tag(product.ketogenic_tag())
            .build()
        ).toList();
    }

    private Boolean isBundled(List<ProductDto> productDtos){
        return productDtos.stream()
            .map(productDto -> productDto.category())
            .distinct()
            .count() > ONE_CATEGOTY;
    }

    public ProductResponse getProductResponse(Long boardId) {
        List<ProductDto> productDtos = boardRepository.getProductDto(boardId);

        Optional.ofNullable(productDtos).orElseThrow(() ->
            new BbangleException(BOARD_NOT_FOUND));

        List<BoardDetailProductDto> boardDetailProductDtos = toProductToResponse(productDtos);
        Boolean isBundled = isBundled(productDtos);

        return ProductResponse.builder()
            .products(boardDetailProductDtos)
            .boardIsBundled(isBundled)
            .build();
    }

    public Slice<BoardResponseDto> getPostInFolder(
        Long memberId,
        String sort,
        Long folderId,
        Pageable pageable
    ) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));

        WishListFolder folder = folderRepository.findByMemberAndId(member, folderId)
            .orElseThrow(() -> new BbangleException("존재하지 않는 폴더입니다."));

        return boardRepository.getAllByFolder(sort, pageable, folderId, folder);
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
