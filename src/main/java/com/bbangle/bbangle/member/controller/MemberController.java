package com.bbangle.bbangle.member.controller;

import com.bbangle.bbangle.member.dto.MemberInfoRequest;
import com.bbangle.bbangle.member.service.MemberService;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
        MultipartFile profileImage
    ) {
        Long memberId = SecurityUtils.getMemberId();

        memberService.updateMemberInfo(additionalInfo, memberId, profileImage);

        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

}
