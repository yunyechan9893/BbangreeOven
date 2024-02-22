package com.bbangle.bbangle.service;

import com.bbangle.bbangle.dto.ProfileInfoResponseDto;

public interface ProfileService {
    ProfileInfoResponseDto getProfileInfo(Long memberId);

    void doubleCheckNickname(String nickname);
}
