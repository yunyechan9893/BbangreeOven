package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.MessageResDto;
import com.bbangle.bbangle.dto.ProfileInfoResponseDto;
import com.bbangle.bbangle.exception.DuplicateNicknameException;
import com.bbangle.bbangle.exception.MemberNotFoundException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.repository.ProfileRepository;
import com.bbangle.bbangle.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public ProfileInfoResponseDto getProfileInfo(Long memberId) {
        Member member = profileRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return ProfileInfoResponseDto.builder()
                .profileImg(member.getProfile())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhone())
                .birthDate(member.getBirth())
                .build();
    }

    public MessageResDto updateProfileInfo(ProfileInfoResponseDto profileInfoResponseDto, Long memberId) {
        return null;
    }

    @Override
    public void doubleCheckNickname(String nickname) {
        Optional<Member> member = profileRepository.findByNickname(nickname);
        if(!member.isEmpty()){
            throw new DuplicateNicknameException();
        }
    }
}
