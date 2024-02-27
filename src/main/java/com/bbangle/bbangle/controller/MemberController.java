package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.MemberInfoRequest;
import com.bbangle.bbangle.dto.NicknameCheckResponse;
import com.bbangle.bbangle.service.MemberService;
import com.bbangle.bbangle.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    
    @PutMapping()
    public ResponseEntity<Void> updateInfo(@RequestBody MemberInfoRequest request){
        Long memberId = SecurityUtils.getMemberId();

        memberService.updateMemberInfo(request, memberId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
