package com.bbangle.bbangle.service;

import java.time.LocalDateTime;
import com.bbangle.bbangle.config.ranking.BoardLikeInfo;
import com.bbangle.bbangle.dto.WishProductRequestDto;
import com.bbangle.bbangle.exception.MemberNotFoundException;
import com.bbangle.bbangle.model.Board;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.model.WishlistFolder;
import com.bbangle.bbangle.model.WishlistProduct;
import com.bbangle.bbangle.repository.BoardRepository;
import com.bbangle.bbangle.repository.MemberRepository;
import com.bbangle.bbangle.repository.WishListFolderRepository;
import com.bbangle.bbangle.repository.WishListProductRepository;
import com.bbangle.bbangle.util.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListProductService {

    private final MemberRepository memberRepository;
    private final WishListFolderRepository wishListFolderRepository;
    private final WishListProductRepository wishListProductRepository;
    private final BoardRepository boardRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void wish(Long memberId, Long boardId, WishProductRequestDto wishRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        WishlistFolder wishlistFolder = wishListFolderRepository.findByMemberAndId(member, wishRequest.folderId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 폴더입니다."));

        wishListProductRepository.findByBoardAndFolderId(boardId, wishlistFolder)
            .ifPresentOrElse(
                product -> {
                    boolean status = product.updateWishStatus();
                    product.getBoard().updateWishCnt(status);

                    if(status){
                        redisTemplate.opsForZSet().incrementScore(RedisKeyUtil.POPULAR_KEY, boardId, 1);
                        redisTemplate.opsForZSet().incrementScore(RedisKeyUtil.RECOMMEND_KEY, boardId, 1);
                        redisTemplate.opsForList().rightPush(String.valueOf(boardId), new BoardLikeInfo(true, LocalDateTime.now()));
                    } else {
                        redisTemplate.opsForZSet().incrementScore(RedisKeyUtil.POPULAR_KEY, boardId, -1);
                        redisTemplate.opsForZSet().incrementScore(RedisKeyUtil.RECOMMEND_KEY, boardId, 1);
                        redisTemplate.opsForList().rightPush(String.valueOf(boardId), new BoardLikeInfo(false, LocalDateTime.now()));
                    }
                },
                makeNewWish(boardId, wishlistFolder)
            );
    }

    private Runnable makeNewWish(Long boardId, WishlistFolder wishlistFolder) {
        return () -> {
            redisTemplate.opsForZSet().incrementScore(RedisKeyUtil.POPULAR_KEY, boardId, 1);
            redisTemplate.opsForZSet().incrementScore(RedisKeyUtil.RECOMMEND_KEY, boardId, 1);
            redisTemplate.opsForList().rightPush(String.valueOf(boardId), new BoardLikeInfo(true, LocalDateTime.now()));
            Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
            WishlistProduct wishlistProduct = WishlistProduct.builder()
                .wishlistFolder(wishlistFolder)
                .board(board)
                .isDeleted(false)
                .build();
            WishlistProduct save = wishListProductRepository.save(wishlistProduct);
            save.getBoard().updateWishCnt(true);
        };
    }

}
