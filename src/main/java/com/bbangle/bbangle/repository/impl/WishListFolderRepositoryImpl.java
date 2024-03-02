package com.bbangle.bbangle.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import com.bbangle.bbangle.dto.FolderResponseDto;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.model.QBoard;
import com.bbangle.bbangle.model.QWishlistFolder;
import com.bbangle.bbangle.model.QWishlistProduct;
import com.bbangle.bbangle.repository.queryDsl.WishListFolderQueryDSLRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WishListFolderRepositoryImpl implements WishListFolderQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FolderResponseDto> findMemberFolderList(Member member) {
        QWishlistFolder folder = QWishlistFolder.wishlistFolder;
        QWishlistProduct wishedBoard = QWishlistProduct.wishlistProduct;
        QBoard board = QBoard.board;

        List<Tuple> fetch = queryFactory.select(
                folder.id,
                folder.folderName,
                board.profile)
            .from(folder)
            .leftJoin(wishedBoard).on(wishedBoard.wishlistFolder.eq(folder))
            .leftJoin(board).on(wishedBoard.board.eq(board))
            .where(folder.member.eq(member)
                .and(folder.isDeleted.eq(false))
                .and(wishedBoard.isDeleted.eq(false).or(wishedBoard.isNull())))
            .fetch();

        Map<Long, List<Tuple>> groupedByFolderId = fetch.stream()
            .collect(Collectors.groupingBy(tuple -> tuple.get(folder.id)));

        return groupedByFolderId.entrySet().stream()
            .map(entry -> {
                Long folderId = entry.getKey();
                List<Tuple> tuples = entry.getValue();

                String title = tuples.get(0).get(folder.folderName);
                int count = tuples.size();
                List<String> productImages = tuples.stream()
                    .map(tuple -> tuple.get(board.profile))
                    .filter(Objects::nonNull)
                    .limit(4)
                    .collect(Collectors.toList());

                return new FolderResponseDto(folderId, title, count, productImages);
            })
            .collect(Collectors.toList());

    }

}
