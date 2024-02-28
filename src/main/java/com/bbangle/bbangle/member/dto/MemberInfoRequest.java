package com.bbangle.bbangle.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberInfoRequest(
    @Size(max = 20, message = "nickname 은 20자 이하만 등록 가능합니다.")
    @NotNull(message = "닉네임은 필수입니다.")
    String nickname,
    String phone,
    String birthDate,
    boolean isAllowingMarketing,
    @NotNull(message = "개인정보 약관 동의는 필수입니다.")
    boolean isPersonalInfoConsented,
    @NotNull(message = "서비스 이용 약관 동의는 필수입니다.")
    boolean isTermsOfServiceAccepted

) {

}
