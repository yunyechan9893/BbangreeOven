package com.bbangle.bbangle.admin.service;

import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.wishlist.repository.WishListBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final MemberRepository memberRepository;
    private final WishListBoardRepository wishListBoardRepository;


    public Long countAllMember() {
        return memberRepository.count();
    }


    public Long countNewMember() {
        return memberRepository.countNewMember(7);
    }


    public Double countMembersUsingWishlist() {
        long wishlistUsersCount = wishListBoardRepository.countMembersUsingWishlist();
        long memberCount = memberRepository.count();

        if (memberCount == 0L) {
            throw new IllegalArgumentException("0으로 나눌 수 없습니다.");
        }

        return (double) wishlistUsersCount / memberCount;
    }

}
