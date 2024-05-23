package com.bbangle.bbangle.analytics.service;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.repository.BoardRepository;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.review.repository.ReviewRepository;
import com.bbangle.bbangle.wishlist.repository.WishListBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final WishListBoardRepository wishListBoardRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final BoardRepository boardRepository;


    public Long countAllMember() {
        return memberRepository.count();
    }


    public Long countNewMember() {
        return memberRepository.countNewMember(7);
    }


    public String countMembersUsingWishlist() {
        long wishlistUsersCount = wishListBoardRepository.countMembersUsingWishlist();
        long memberCount = memberRepository.count();

        if (memberCount == 0L) {
            throw new IllegalArgumentException("0으로 나눌 수 없습니다.");
        }

        double result = ((double) wishlistUsersCount / memberCount) * 100;

        return String.format("%.2f", result);
    }


    public List<Board> getWishlistBoardRanking() {
        return boardRepository.getWishlistRanking();
    }


    public Long countWishlistBoardByPeriod(LocalDate startDate, LocalDate endDate) {
        return wishListBoardRepository.countWishlistByPeriod(startDate, endDate);

    }


    public Double calculateReviewUsingRatio() {
        Long reviewUsersCount = reviewRepository.countMembersUsingReview();
        long memberCount = memberRepository.count();

        if (memberCount == 0L) {
            throw new IllegalArgumentException("0으로 나눌 수 없습니다.");
        }

        return (double) reviewUsersCount / memberCount;
    }


    public Long countReviewByPeriod(LocalDate startDate, LocalDate endDate) {
        return reviewRepository.countReviewByPeriod(startDate, endDate);
    }
}
