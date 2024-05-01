package com.bbangle.bbangle.wishlist.repository;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.wishlist.domain.WishlistFolder;
import com.bbangle.bbangle.wishlist.domain.WishlistBoard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListProductRepository extends JpaRepository<WishlistBoard, Long> {

    @Query(
        value = "select wish from WishlistBoard wish where wish.board.id = :boardId and wish.wishlistFolder = :folder"
    )
    Optional<WishlistBoard> findByBoardAndFolderId(
        @Param("boardId")
        Long boardId,
        @Param("folder")
        WishlistFolder folder
    );

    @Query(
        value = "select wish from WishlistBoard wish where wish.memberId = :memberId and wish.isDeleted = false"
    )
    Optional<List<WishlistBoard>> findByMemberId(@Param("memberId") Long memberId);

    @Query(value = "select wish from WishlistBoard wish where wish.board.id = :boardId and wish.memberId = :memberId")
    Optional<WishlistBoard> findByBoardId(Long boardId, Long memberId);

    boolean existsByBoardAndMemberId(Board board, Long memberId);

}
