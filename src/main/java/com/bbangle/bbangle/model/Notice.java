package com.bbangle.bbangle.model;

import com.bbangle.bbangle.dto.NoticeResponseDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "notice")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notice{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @CreationTimestamp
    private LocalDateTime createdAt;


    public static NoticeResponseDto toDto(Notice notice){
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .createdAt(String.valueOf(notice.getCreatedAt()))
                .build();
    }
}
