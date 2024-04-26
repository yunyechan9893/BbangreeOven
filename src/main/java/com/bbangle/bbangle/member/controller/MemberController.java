package com.bbangle.bbangle.member.controller;

import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.message.MessageResDto;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.member.dto.WithdrawalRequestDto;
import com.bbangle.bbangle.member.dto.MemberInfoRequest;
import com.bbangle.bbangle.member.service.MemberService;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final ResponseService responseService;

    @PutMapping("additional-information")
    public CommonResult updateInfo(
        @RequestPart
        MemberInfoRequest additionalInfo,
        @RequestPart(required = false)
        MultipartFile profileImg
    ) {
        Long memberId = SecurityUtils.getMemberId();

        memberService.updateMemberInfo(additionalInfo, memberId, profileImg);

        return responseService.getSuccessResult();
    }

    @PatchMapping
    public CommonResult deleteMember(@RequestBody WithdrawalRequestDto withdrawalRequestDto){
        String deleteSuccessMsg = "회원 탈퇴에 성공했습니다";
        Long memberId = SecurityUtils.getMemberId();
        memberService.saveDeleteReason(withdrawalRequestDto, memberId);
        memberService.deleteMember(memberId);
        return responseService.getSuccessResult(deleteSuccessMsg, 0);
    }
}
