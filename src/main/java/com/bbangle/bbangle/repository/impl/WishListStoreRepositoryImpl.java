package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.dto.QWishListStoreResponseDto;
import com.bbangle.bbangle.dto.WishListStoreResponseDto;
import com.bbangle.bbangle.model.WishlistStore;
import com.bbangle.bbangle.repository.queryDsl.WishListStoreQueryDSLRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.bbangle.bbangle.model.QStore.store;
import static com.bbangle.bbangle.model.QWishlistStore.wishlistStore;

@Repository
public class WishListStoreRepositoryImpl implements WishListStoreQueryDSLRepository {
    private final JPAQueryFactory queryFactory;
    public WishListStoreRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<WishlistStore> findWishListStores(Long memberId) {
        return queryFactory
                .selectFrom(wishlistStore)
                .where(wishlistStore.member.id.eq(memberId)
                        .and(wishlistStore.isDeleted.eq(false)))
                .fetch();
    }

    @Override
    public Page<WishListStoreResponseDto> getWishListStoreRes(Long memberId, Pageable pageable) {
        List<WishListStoreResponseDto> wishListStores = queryFactory
                .select(new QWishListStoreResponseDto(
                        store.introduce,
                        store.name.as("storeName"),
                        wishlistStore.store.id.as("storeId")
                ))
                .from(wishlistStore)
                .leftJoin(wishlistStore.store, store)
                .where(wishlistStore.member.id.eq(memberId).and(wishlistStore.isDeleted.ne(true)))
                .orderBy(wishlistStore.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<WishListStoreResponseDto> countQuery = queryFactory
                .select(new QWishListStoreResponseDto(
                        store.introduce,
                        store.name.as("storeName"),
                        wishlistStore.store.id.as("storeId")
                ))
                .from(wishlistStore)
                .leftJoin(wishlistStore.store, store)
                .where(wishlistStore.member.id.eq(memberId).and(wishlistStore.isDeleted.ne(true)))
                .orderBy(wishlistStore.createdAt.desc());

        return PageableExecutionUtils.getPage(wishListStores, pageable, countQuery::fetchCount);
    }

    @Override
    public Optional<WishlistStore> findWishListStore(Long memberId, Long storeId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(wishlistStore)
                .where(wishlistStore.member.id.eq(memberId)
                        .and(wishlistStore.store.id.eq(storeId)))
                .fetchOne());
    }
}
