package com.bbangle.bbangle.wishlist.domain;

import com.bbangle.bbangle.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "wishlist_product")
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishListBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wishlist_folder_id")
    private Long wishlistFolderId;

    @Column(name = "product_board_id")
    private Long boardId;

    @Column(name = "member_id")
    private Long memberId;

}
