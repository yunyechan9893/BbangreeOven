package com.bbangle.bbangle.wishListBoard.domain;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.common.domain.BaseEntity;
import com.bbangle.bbangle.wishListFolder.domain.WishlistFolder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "wishlist_product")
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_folder_id")
    private WishlistFolder wishlistFolder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_board_id")
    private Board board;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "is_deleted", columnDefinition = "tinyint")
    private boolean isDeleted;

    public boolean updateWishStatus() {
        isDeleted = !isDeleted;
        return isDeleted;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
