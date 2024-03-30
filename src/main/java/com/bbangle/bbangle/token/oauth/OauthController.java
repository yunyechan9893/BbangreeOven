package com.bbangle.bbangle.token.oauth;

import com.bbangle.bbangle.token.oauth.domain.OauthServerType;
import com.bbangle.bbangle.token.oauth.infra.kakao.dto.LoginTokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/login/{oauthServerType}")
    ResponseEntity<LoginTokenResponse> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam("code") String code
    ){
        LoginTokenResponse loginTokenResponse = oauthService.login(oauthServerType, code);
        return ResponseEntity.ok(loginTokenResponse);
    }

}
