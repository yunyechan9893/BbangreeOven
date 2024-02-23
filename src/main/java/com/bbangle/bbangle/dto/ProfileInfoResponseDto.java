package com.bbangle.bbangle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

public record ProfileInfoResponseDto(
        String profileImg,
        String nickname,
        String birthDate,
        String phoneNumber
) {
    @Builder
    public ProfileInfoResponseDto(String profileImg, String nickname, String birthDate, String phoneNumber) {
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
