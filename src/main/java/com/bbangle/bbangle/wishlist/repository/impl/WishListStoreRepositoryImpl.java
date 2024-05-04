package com.bbangle.bbangle.wishlist.repository.impl;

import static com.bbangle.bbangle.store.domain.QStore.store;

import com.bbangle.bbangle.wishlist.domain.QWishListStore;
import com.bbangle.bbangle.wishlist.domain.WishListStore;
import com.bbangle.bbangle.wishlist.repository.WishListStoreQueryDSLRepository;
import com.bbangle.bbangle.wishlist.dto.QWishListStoreResponseDto;
import com.bbangle.bbangle.wishlist.dto.WishListStoreResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WishListStoreRepositoryImpl implements WishListStoreQueryDSLRepository {
    private final JPAQueryFactory queryFactory;
    private static final QWishListStore wishListStore = QWishListStore.wishListStore;

    @Override
    public List<WishListStore> findWishListStores(Long memberId) {
        return queryFactory
                .selectFrom(wishListStore)
                .where(wishListStore.member.id.eq(memberId)
                        .and(wishListStore.isDeleted.eq(false)))
                .fetch();
    }

    @Override
    public Page<WishListStoreResponseDto> getWishListStoreResponse(Long memberId, Pageable pageable) {
        List<WishListStoreResponseDto> wishListStores = queryFactory
                .select(new QWishListStoreResponseDto(
                        store.introduce,
                        store.name.as("storeName"),
                        store.id.as("storeId"),
                        store.profile
                ))
                .from(wishListStore)
                .leftJoin(wishListStore.store, store)
                .where(wishListStore.member.id.eq(memberId).and(wishListStore.isDeleted.ne(true)))
                .orderBy(wishListStore.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(wishListStore.id.count())
                .from(wishListStore)
                .leftJoin(wishListStore.store, store)
                .where(wishListStore.member.id.eq(memberId).and(wishListStore.isDeleted.ne(true)))
                .orderBy(wishListStore.createdAt.desc());

        return PageableExecutionUtils.getPage(wishListStores, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<WishListStore> findWishListStore(Long memberId, Long storeId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(wishListStore)
                .where(wishListStore.member.id.eq(memberId)
                        .and(wishListStore.store.id.eq(storeId)))
                .fetchFirst());
    }
}
