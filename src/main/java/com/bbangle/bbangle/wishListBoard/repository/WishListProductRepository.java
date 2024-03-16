package com.bbangle.bbangle.wishListBoard.repository;

import com.bbangle.bbangle.wishListFolder.domain.WishlistFolder;
import com.bbangle.bbangle.wishListBoard.domain.WishlistProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListProductRepository extends JpaRepository<WishlistProduct, Long> {

    @Query(
        value = "select wish from WishlistProduct wish where wish.board.id = :boardId and wish.wishlistFolder = :folder"
    )
    Optional<WishlistProduct> findByBoardAndFolderId(
        @Param("boardId")
        Long boardId,
        @Param("folder")
        WishlistFolder folder
    );

    @Query(
        value = "select wish from WishlistProduct wish where wish.memberId = :memberId and wish.isDeleted = false"
    )
    Optional<List<WishlistProduct>> findByMemberId(@Param("memberId") Long memberId);

    @Query(value = "select wish from WishlistProduct wish where wish.board.id = :boardId and wish.memberId = :memberId")
    Optional<WishlistProduct> findByBoardId(Long boardId, Long memberId);

}
