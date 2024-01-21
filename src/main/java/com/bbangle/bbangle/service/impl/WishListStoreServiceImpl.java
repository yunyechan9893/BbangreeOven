package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.model.WishlistStore;
import com.bbangle.bbangle.repository.MemberRepository;
import com.bbangle.bbangle.repository.StoreRepository;
import com.bbangle.bbangle.repository.WishListStoreRepository;
import com.bbangle.bbangle.service.WishListStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListStoreServiceImpl implements WishListStoreService {
    private final WishListStoreRepository wishListStoreRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Override
    public WishListStoreResponseDto getWishListStoresRes(Long memberId) {
        return null;
    }

    public void save(Long memberId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("no store about " + storeId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("no member about " + memberId));
        wishListStoreRepository.save(WishlistStore.builder()
                .member(member)
                .store(store)
                .build());
    }
}
