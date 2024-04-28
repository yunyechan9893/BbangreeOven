package com.bbangle.bbangle.member.controller;

import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.dto.MessageDto;
import com.bbangle.bbangle.common.message.MessageResDto;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.member.dto.ProfileInfoResponseDto;
import com.bbangle.bbangle.member.dto.InfoUpdateRequest;
import com.bbangle.bbangle.member.service.ProfileServiceImpl;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class ProfileController {

    private final ProfileServiceImpl profileService;
    private final ResponseService responseService;

    /**
     * 프로필 조회
     *
     * @return 프로필 정보
     */
    @GetMapping
    public CommonResult getProfile(){
        Long memberId = SecurityUtils.getMemberId();
        ProfileInfoResponseDto profileInfo = profileService.getProfileInfo(memberId);
        return responseService.getSingleResult(profileInfo);
    }


    /**
     * 닉네임 중복 확인
     *
     * @param nickname 닉네임
     * @return 메세지
     */
    @GetMapping("/doublecheck")
    public CommonResult doubleCheckNickname(@RequestParam String nickname){
        Long memberId = SecurityUtils.getMemberId();
        Assert.notNull(memberId, "권한이 없습니다");
        if(nickname.isEmpty() || nickname == null){
            return responseService.getSingleResult(new MessageDto("닉네임을 입력해주세요!", false));
        }
        if(nickname.length() > 20){
            return responseService.getSingleResult(new MessageDto("닉네임은 20자 제한이에요!", false));
        }
        String existedNickname = profileService.doubleCheckNickname(nickname);
        if (!existedNickname.isEmpty()){
            return responseService.getSingleResult(new MessageDto("중복된 닉네임이에요", false));
        }
        return responseService.getSingleResult(new MessageDto("사용가능한 닉네임이에요!", true));
    }

    @PutMapping
    public CommonResult update(
        @RequestPart(required = false)
        InfoUpdateRequest infoUpdateRequest,
        @RequestPart(required = false)
        MultipartFile profileImg
    ) {
        Long memberId = SecurityUtils.getMemberId();
        profileService.updateProfileInfo(infoUpdateRequest, memberId, profileImg);
        return responseService.getSuccessResult();
    }
}
