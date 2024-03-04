package com.bbangle.bbangle.member.dto;

public record InfoUpdateRequest(
    String nickname,
    String phoneNumber,
    String birthDate
) {

}
