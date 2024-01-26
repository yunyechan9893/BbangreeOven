package com.bbangle.bbangle.repository;

import java.util.List;
import java.util.Optional;
import com.bbangle.bbangle.model.WishlistFolder;
import com.bbangle.bbangle.model.WishlistProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListProductRepository extends JpaRepository<WishlistProduct, Long> {

    @Query(value = "select wish from WishlistProduct wish where wish.board.id = :boardId and wish.wishlistFolder = :folder")
    Optional<WishlistProduct> findByBoardAndFolderId(@Param("boardId") Long boardId, @Param("folder")WishlistFolder folder);

    List<WishlistProduct> findAllByWishlistFolder(WishlistFolder wishlistFolder);
}
