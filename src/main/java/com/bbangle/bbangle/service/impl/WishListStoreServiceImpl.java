package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.exception.NoSuchMemberidOrStoreIdException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.model.WishlistStore;
import com.bbangle.bbangle.repository.StoreRepository;
import com.bbangle.bbangle.repository.WishListStoreRepository;
import com.bbangle.bbangle.repository.impl.WishListStoreRepositoryImpl;
import com.bbangle.bbangle.service.WishListStoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListStoreServiceImpl implements WishListStoreService {

    private final WishListStoreRepository wishListStoreRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final WishListStoreRepositoryImpl wishListStoreRepositoryImpl;

    @Override
    @Transactional(readOnly = true)
    public List<WishListStoreResponseDto> getWishListStoresRes(Long memberId) {
        return wishListStoreRepositoryImpl.getWishListStoreRes(memberId);

    }

    @Transactional
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

    @Transactional
    public void deleteStore(Long memberId, Long storeId) {
        WishlistStore wishListStore = wishListStoreRepositoryImpl.findWishListStore(memberId,
                storeId)
            .orElseThrow(() -> new NoSuchMemberidOrStoreIdException());
        wishListStore.delete();
    }

}
