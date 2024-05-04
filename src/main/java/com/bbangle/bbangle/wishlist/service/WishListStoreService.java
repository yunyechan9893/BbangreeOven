package com.bbangle.bbangle.wishlist.service;

import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_MEMBER;
import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_WISH_INFO;

import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.store.domain.Store;
import com.bbangle.bbangle.store.repository.StoreRepository;
import com.bbangle.bbangle.wishlist.domain.WishListStore;
import com.bbangle.bbangle.wishlist.dto.WishListStorePagingDto;
import com.bbangle.bbangle.wishlist.repository.WishListStoreRepository;
import com.bbangle.bbangle.wishlist.repository.impl.WishListStoreRepositoryImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListStoreService {

    private final WishListStoreRepository wishListStoreRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final WishListStoreRepositoryImpl wishListStoreRepositoryImpl;

    @Transactional(readOnly = true)
    public WishListStorePagingDto getWishListStoresResponse(Long memberId, Pageable pageable) {
        return WishListStorePagingDto.of(wishListStoreRepositoryImpl.getWishListStoreResponse(memberId, pageable));

    }

    @Transactional
    public void save(Long memberId, Long storeId) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new BbangleException("존재하지 않는 스토어 입니다"));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));
        wishListStoreRepository.save(WishListStore.builder()
            .member(member)
            .store(store)
            .build());
    }

    @Transactional
    public void deleteStore(Long memberId, Long storeId) {
        WishListStore wishListStore = wishListStoreRepositoryImpl.findWishListStore(memberId,
                storeId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_WISH_INFO));
        wishListStore.delete();
    }

    @Transactional
    public void deletedByDeletedMember(Long memberId) {
        List<WishListStore> wishListStores = wishListStoreRepositoryImpl.findWishListStores(memberId);
        if (wishListStores.size() !=0){
            for (WishListStore wishListStore : wishListStores) {
                wishListStore.delete();
            }
        }
    }
}
