package com.bbangle.bbangle.service;

import com.bbangle.bbangle.config.ranking.BoardLikeInfo;
import com.bbangle.bbangle.config.ranking.ScoreType;
import com.bbangle.bbangle.dto.WishProductRequestDto;
import com.bbangle.bbangle.exception.MemberNotFoundException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.model.Board;
import com.bbangle.bbangle.model.WishlistFolder;
import com.bbangle.bbangle.model.WishlistProduct;
import com.bbangle.bbangle.repository.BoardRepository;
import com.bbangle.bbangle.repository.WishListFolderRepository;
import com.bbangle.bbangle.repository.WishListProductRepository;
import com.bbangle.bbangle.util.RedisKeyUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListProductService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH");


    private final MemberRepository memberRepository;
    private final WishListFolderRepository wishListFolderRepository;
    private final WishListProductRepository wishListProductRepository;
    private final BoardRepository boardRepository;
    @Qualifier("defaultRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    @Qualifier("boardLikeInfoRedisTemplate")
    private final RedisTemplate<String, Object> boardLikeInfoRedisTemplate;

    @Transactional
    public void wish(Long memberId, Long boardId, WishProductRequestDto wishRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        WishlistFolder wishlistFolder = wishListFolderRepository.findByMemberAndId(member,
                wishRequest.folderId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 폴더입니다."));

        wishListProductRepository.findByBoardAndFolderId(boardId, wishlistFolder)
            .ifPresentOrElse(
                product -> {
                    boolean status = product.updateWishStatus();
                    product.getBoard()
                        .updateWishCnt(status);

                    if (status) {
                        redisTemplate.opsForZSet()
                            .incrementScore(RedisKeyUtil.POPULAR_KEY, String.valueOf(boardId), 1);
                        redisTemplate.opsForZSet()
                            .incrementScore(RedisKeyUtil.RECOMMEND_KEY, String.valueOf(boardId), 1);
                        boardLikeInfoRedisTemplate.opsForList()
                            .rightPush(LocalDateTime.now()
                                    .format(formatter),
                                new BoardLikeInfo(boardId, 1, LocalDateTime.now(), ScoreType.WISH));
                    } else {
                        redisTemplate.opsForZSet()
                            .incrementScore(RedisKeyUtil.POPULAR_KEY, String.valueOf(boardId), -1);
                        redisTemplate.opsForZSet()
                            .incrementScore(RedisKeyUtil.RECOMMEND_KEY, String.valueOf(boardId),
                                -1);
                        boardLikeInfoRedisTemplate.opsForList()
                            .rightPush(LocalDateTime.now()
                                    .format(formatter),
                                new BoardLikeInfo(boardId, 1, LocalDateTime.now(), ScoreType.WISH));
                    }
                },
                makeNewWish(boardId, wishlistFolder, member)
            );
    }

    private Runnable makeNewWish(Long boardId, WishlistFolder wishlistFolder, Member member) {
        return () -> {
            redisTemplate.opsForZSet()
                .incrementScore(RedisKeyUtil.POPULAR_KEY, String.valueOf(boardId), 1);
            redisTemplate.opsForZSet()
                .incrementScore(RedisKeyUtil.RECOMMEND_KEY, String.valueOf(boardId), 1);
            boardLikeInfoRedisTemplate.opsForList()
                .rightPush(LocalDateTime.now()
                        .format(formatter),
                    new BoardLikeInfo(boardId, 1, LocalDateTime.now(), ScoreType.WISH));
            Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
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

}
