package com.bbangle.bbangle.member.service;

import com.bbangle.bbangle.member.dto.ProfileInfoResponseDto;

public interface ProfileService {

    ProfileInfoResponseDto getProfileInfo(Long memberId);

    String doubleCheckNickname(String nickname);

}
