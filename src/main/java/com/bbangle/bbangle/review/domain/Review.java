package com.bbangle.bbangle.review.domain;


import com.bbangle.bbangle.common.domain.Badge;
import com.bbangle.bbangle.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "review")
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //동현님 말대로 연관매핑 안해보고 해보기
    @Column(name = "member_id")
    @NotNull
    private Long memberId;

    @Column(name = "board_id")
    @NotNull
    private Long boardId;

    @NotNull
    private String badge1;
    @NotNull
    private String badge2;
    @NotNull
    private String badge3;
    @NotNull
    private BigDecimal rate;

    private String content;

    //TODO 리뷰는 일단 나중에 데이터로 필요할 꺼 같아 is_deleted 추가
    @Column(name = "is_deleted", columnDefinition = "tinyint")
    private boolean isDeleted;

    public void insertBadge(Badge badge){
        switch(badge){
            case GOOD, BAD -> this.badge1 = badge.name();
            case SWEET,PLAIN -> this.badge2 = badge.name();
            case SOFT,HARD -> this.badge3 = badge.name();
        }
    }
}
