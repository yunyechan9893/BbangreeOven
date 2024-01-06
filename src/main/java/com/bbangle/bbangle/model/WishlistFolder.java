package com.bbangle.bbangle.model;

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
import lombok.NoArgsConstructor;

@Table(name = "Wishlist_folder")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistFolder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "folder_name")
    private String folderName;

    @Column(name = "is_deleted", columnDefinition = "tinyint")
    private boolean isDeleted;

}
