package com.bbangle.bbangle.wishlist.repository;

import com.bbangle.bbangle.wishlist.domain.WishListBoard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListBoardRepository extends JpaRepository<WishListBoard, Long> {

    @Query(value = "select wish from WishListBoard wish where wish.memberId = :memberId")
    Optional<List<WishListBoard>> findByMemberId(@Param("memberId") Long memberId);

    boolean existsByBoardIdAndMemberId(Long boardId, Long memberId);

    Optional<WishListBoard> findByBoardIdAndMemberId(Long boardId, Long memberId);

}
