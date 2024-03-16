package com.bbangle.bbangle.member.controller;

import com.bbangle.bbangle.common.message.MessageResDto;
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

    @PutMapping("additional-information")
    public ResponseEntity<Void> updateInfo(
        @RequestPart
        MemberInfoRequest additionalInfo,
        @RequestPart(required = false)
        MultipartFile profileImg
    ) {
        Long memberId = SecurityUtils.getMemberId();

        memberService.updateMemberInfo(additionalInfo, memberId, profileImg);

        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    @PatchMapping
    public ResponseEntity<MessageResDto> deleteMember(@RequestBody WithdrawalRequestDto withdrawalRequestDto){
        Long memberId = SecurityUtils.getMemberId();
        memberService.saveDeleteReason(withdrawalRequestDto, memberId);
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResDto("회원 탈퇴에 성공했습니다"));
    }

}
