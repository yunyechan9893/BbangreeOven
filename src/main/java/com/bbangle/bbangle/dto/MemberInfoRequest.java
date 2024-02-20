package com.bbangle.bbangle.dto;

import jakarta.validation.constraints.Size;

public record MemberInfoRequest (
    @Size(max = 20, message = "nickname 은 20자 이하만 등록 가능합니다.")
    String nickname,
    String phone,
    String birthDate

){

}
