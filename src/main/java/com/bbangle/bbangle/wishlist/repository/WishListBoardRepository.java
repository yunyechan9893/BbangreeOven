package com.bbangle.bbangle.wishlist.repository;

import com.bbangle.bbangle.wishlist.domain.WishListBoard;
import com.bbangle.bbangle.wishlist.domain.WishListFolder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListBoardRepository extends JpaRepository<WishListBoard, Long> {

    @Query(
        value = "select wish from WishListBoard wish where wish.board.id = :boardId and wish.wishlistFolder = :folder"
    )
    Optional<WishListBoard> findByBoardAndFolderId(
        @Param("boardId")
        Long boardId,
        @Param("folder")
        WishListFolder folder
    );

    @Query(
        value = "select wish from WishListBoard wish where wish.memberId = :memberId and wish.isDeleted = false"
    )
    Optional<List<WishListBoard>> findByMemberId(@Param("memberId") Long memberId);

    @Query(value = "select wish from WishListBoard wish where wish.board.id = :boardId and wish.memberId = :memberId")
    Optional<WishListBoard> findByBoardId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

}
