package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.MessageResDto;
import com.bbangle.bbangle.dto.ProfileInfoResponseDto;
import com.bbangle.bbangle.service.impl.ProfileServiceImpl;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class ProfileController {

    private final ProfileServiceImpl profileService;
    @GetMapping
    public ResponseEntity<ProfileInfoResponseDto> getProfile(){
        Long memberId = SecurityUtils.getMemberId();
        return ResponseEntity.ok().body(profileService.getProfileInfo(memberId));
    }

    @PatchMapping
    public ResponseEntity<MessageResDto> updateProfileInfo(@RequestBody ProfileInfoResponseDto profileInfoResponseDto){
        Long memberId = SecurityUtils.getMemberId();
        Assert.notNull(profileInfoResponseDto.nickname(), "닉네임은 필수입니다");
        Assert.notNull(profileInfoResponseDto.phoneNumber(), "휴대폰 번호는 필수입니다");
        return ResponseEntity.ok().body(profileService.updateProfileInfo(profileInfoResponseDto, memberId));
    }


}
