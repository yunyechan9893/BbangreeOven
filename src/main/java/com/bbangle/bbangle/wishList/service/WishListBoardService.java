package com.bbangle.bbangle.wishList.service;

import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_MEMBER;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.repository.BoardRepository;
import com.bbangle.bbangle.config.ranking.BoardLikeInfo;
import com.bbangle.bbangle.config.ranking.ScoreType;
import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.wishList.repository.WishListProductRepository;
import com.bbangle.bbangle.wishList.domain.WishlistProduct;
import com.bbangle.bbangle.wishList.dto.WishProductRequestDto;
import com.bbangle.bbangle.wishList.domain.WishlistFolder;
import com.bbangle.bbangle.wishList.repository.WishListFolderRepository;
import com.bbangle.bbangle.ranking.domain.Ranking;
import com.bbangle.bbangle.ranking.repository.RankingRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListBoardService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");

    private final MemberRepository memberRepository;
    private final WishListFolderRepository wishListFolderRepository;
    private final WishListProductRepository wishListProductRepository;
    private final BoardRepository boardRepository;
    private final RankingRepository rankingRepository;
    @Qualifier("defaultRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    @Qualifier("boardLikeInfoRedisTemplate")
    private final RedisTemplate<String, Object> boardLikeInfoRedisTemplate;

    @Transactional
    public void wish(Long memberId, Long boardId, WishProductRequestDto wishRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));

        WishlistFolder wishlistFolder = wishListFolderRepository.findByMemberAndId(member,
                wishRequest.folderId())
            .orElseThrow(() -> new BbangleException("존재하지 않는 폴더입니다."));

        wishListProductRepository.findByBoardAndFolderId(boardId, wishlistFolder)
            .ifPresentOrElse(
                product -> {
                    if (!product.isDeleted()) {
                        throw new BbangleException("이미 위시리스트 폴더에 있는 게시물입니다.");
                    }
                    boolean status = product.updateWishStatus();
                    product.getBoard()
                        .updateWishCnt(status);
                    if (status) {
                        updateRankingScore(boardId, 1.0);
                    }
                },
                makeNewWish(boardId, wishlistFolder, member)
            );
    }

    private Runnable makeNewWish(Long boardId, WishlistFolder wishlistFolder, Member member) {
        return () -> {
            updateRankingScore(boardId, 1.0);
            Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BbangleException("존재하지 않는 게시글입니다."));
            WishlistProduct wishlistProduct = WishlistProduct.builder()
                .wishlistFolder(wishlistFolder)
                .board(board)
                .memberId(member.getId())
                .isDeleted(false)
                .build();
            WishlistProduct save = wishListProductRepository.save(wishlistProduct);
            save.getBoard()
                .updateWishCnt(true);
        };
    }

    private void updateRankingScore(Long boardId, Double updatingScore) {
        Ranking ranking = rankingRepository.findByBoardId(boardId)
            .orElseThrow(
                () -> new BbangleException(BbangleErrorCode.RANKING_NOT_FOUND));
        ranking.updateRecommendScore(updatingScore);
        ranking.updatePopularScore(updatingScore);
        boardLikeInfoRedisTemplate.opsForList()
            .rightPush(LocalDateTime.now()
                    .format(formatter),
                new BoardLikeInfo(boardId, updatingScore, LocalDateTime.now(), ScoreType.WISH));
    }

    @Transactional
    public void deletedByDeletedMember(Long memberId) {
        Optional<List<WishlistProduct>> wishlistProducts = wishListProductRepository.findByMemberId(
            memberId);
        if (wishlistProducts.isPresent()) {
            for (WishlistProduct wishlistProduct : wishlistProducts.get()) {
                wishlistProduct.delete();
            }
        }
    }

    @Transactional
    public void cancel(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));

        WishlistProduct product = wishListProductRepository.findByBoardId(boardId, memberId)
            .orElseThrow(() -> new BbangleException("사용자의 위시리스트 항목에 존재하지 않는 게시글입니다."));

        if (product.isDeleted()) {
            throw new BbangleException("이미 위시리스트 항목에서 삭제된 게시글입니다.");
        }

        product.updateWishStatus();

        updateRankingScore(boardId, -1.0);
    }

}
