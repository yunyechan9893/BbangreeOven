package com.bbangle.bbangle.ranking.domain;

import com.bbangle.bbangle.board.domain.Board;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ranking")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ranking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_board_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Board board;

    @Column(name = "popular_score")
    private Double popularScore;

    @Column(name = "recommend_score")
    private Double recommendScore;

    public void updatePopularScore(Double score) {
        this.popularScore += score;
    }

    public void updateRecommendScore(Double score) {
        this.recommendScore += score;
    }

}
