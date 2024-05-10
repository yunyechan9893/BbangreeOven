package com.bbangle.bbangle.wishlist.service;

import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_MEMBER;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.repository.BoardRepository;
import com.bbangle.bbangle.config.ranking.BoardLikeInfo;
import com.bbangle.bbangle.config.ranking.ScoreType;
import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.wishlist.domain.WishListBoard;
import com.bbangle.bbangle.wishlist.domain.WishListFolder;
import com.bbangle.bbangle.wishlist.repository.WishListBoardRepository;
import com.bbangle.bbangle.wishlist.dto.WishListBoardRequest;
import com.bbangle.bbangle.wishlist.repository.WishListFolderRepository;
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
    private static final String DEFAULT_FOLDER_NAME = "기본 폴더";

    private final MemberRepository memberRepository;
    private final WishListFolderRepository wishListFolderRepository;
    private final WishListBoardRepository wishlistBoardRepository;
    private final BoardRepository boardRepository;
    private final RankingRepository rankingRepository;
    @Qualifier("boardLikeInfoRedisTemplate")
    private final RedisTemplate<String, Object> boardLikeInfoRedisTemplate;

    @Transactional
    public void wish(Long memberId, Long boardId, WishListBoardRequest wishRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));

        WishListFolder wishlistFolder = getWishlistFolder(wishRequest, member);

        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new BbangleException(BbangleErrorCode.BOARD_NOT_FOUND));

        validateIsWishAvailable(memberId, board);

        makeNewWish(board, wishlistFolder, member);
    }


    @Transactional
    public void cancel(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));

        WishListBoard product = wishlistBoardRepository.findByBoardId(boardId, member.getId())
            .orElseThrow(() -> new BbangleException(BbangleErrorCode.WISHLIST_BOARD_NOT_FOUND));

        if (product.isDeleted()) {
            throw new BbangleException(BbangleErrorCode.WISHLIST_BOARD_ALREADY_CANCELED);
        }

        product.updateWishStatus();

        updateRankingScore(boardId, -1.0);
    }

    @Transactional
    public void deletedByDeletedMember(Long memberId) {
        Optional<List<WishListBoard>> wishlistProducts = wishlistBoardRepository.findByMemberId(
            memberId);

        if (wishlistProducts.isPresent()) {
            for (WishListBoard wishlistBoard : wishlistProducts.get()) {
                wishlistBoard.delete();
            }
        }
    }
    private void validateIsWishAvailable(Long memberId, Board board) {
        Optional<WishListBoard> wishListBoard = wishlistBoardRepository.findByBoardId(board.getId(), memberId);

        if (wishListBoard.isPresent() && !wishListBoard.get().isDeleted()) {
            throw new BbangleException(BbangleErrorCode.ALREADY_ON_WISHLIST);
        }
    }

    private WishListFolder getWishlistFolder(
        WishListBoardRequest wishRequest,
        Member member
    ) {
        if (wishRequest.folderId().equals(0L)) {
            return wishListFolderRepository.findByMemberAndFolderName(
                    member, DEFAULT_FOLDER_NAME)
                .orElseThrow(() -> new BbangleException(BbangleErrorCode.FOLDER_NOT_FOUND));
        }

        return wishListFolderRepository.findByMemberAndId(member,
                wishRequest.folderId())
            .orElseThrow(() -> new BbangleException(BbangleErrorCode.FOLDER_NOT_FOUND));
    }

    private void makeNewWish(Board board, WishListFolder wishlistFolder, Member member) {
        updateRankingScore(board.getId(), 1.0);

        WishListBoard wishlistBoard = WishListBoard.builder()
            .wishlistFolder(wishlistFolder)
            .board(board)
            .memberId(member.getId())
            .isDeleted(false)
            .build();

        WishListBoard save = wishlistBoardRepository.save(wishlistBoard);
        save.getBoard()
            .updateWishCnt(true);
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

}
