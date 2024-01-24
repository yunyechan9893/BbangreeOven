package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.dto.QWishListStoreResponseDto;
import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.repository.WishListStoreQueryDSLRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bbangle.bbangle.model.QStore.store;
import static com.bbangle.bbangle.model.QWishlistStore.wishlistStore;

@Repository
public class WishListStoreRepositoryImpl implements WishListStoreQueryDSLRepository {
    private final JPAQueryFactory queryFactory;
    public WishListStoreRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<WishListStoreResponseDto> getWishListStoreRes(Long memberId) {
        return queryFactory
                .select(new QWishListStoreResponseDto(
                        store.introduce,
                        store.name.as("storeName"),
                        wishlistStore.store.id.as("storeId")
                ))
                .from(wishlistStore)
                .leftJoin(wishlistStore.store, store)
                .where(wishlistStore.member.id.eq(memberId))
                .fetch();
    }
}
