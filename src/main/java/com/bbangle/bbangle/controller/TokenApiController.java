package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.TokenDto;
import com.bbangle.bbangle.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenApiController {
    private final TokenService tokenService;

    @GetMapping("/api/token/{id}")
    public ResponseEntity<TokenDto> createNewAccessToken(@PathVariable Long id){
        String newAccessToken = tokenService.createNewAccessToken(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TokenDto(newAccessToken));
    }
}
