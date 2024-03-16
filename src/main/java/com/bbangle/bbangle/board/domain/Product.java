package com.bbangle.bbangle.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Table(name = "product")
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_board_id")
    private Board board;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private int price;

    @Column(name = "category", columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "gluten_free_tag", columnDefinition = "tinyint")
    private boolean glutenFreeTag;

    @Column(name = "high_protein_tag", columnDefinition = "tinyint")
    private boolean highProteinTag;

    @Column(name = "sugar_free_tag", columnDefinition = "tinyint")
    private boolean sugarFreeTag;

    @Column(name = "vegan_tag", columnDefinition = "tinyint")
    private boolean veganTag;

    @Column(name = "ketogenic_tag", columnDefinition = "tinyint")
    private boolean ketogenicTag;

}
