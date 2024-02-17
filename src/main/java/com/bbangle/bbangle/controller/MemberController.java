package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.NicknameCheckResponse;
import com.bbangle.bbangle.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<NicknameCheckResponse> checkNickname(@RequestParam String nickname) {
        NicknameCheckResponse result = NicknameCheckResponse.builder()
            .isUsable(memberService.checkingNickname(nickname))
            .build();

        return ResponseEntity.status(HttpStatus.OK)
            .body(result);

    }

}
