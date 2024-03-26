package com.bbangle.bbangle.config.oauth;

import com.bbangle.bbangle.config.oauth.domain.OauthServerType;
import com.bbangle.bbangle.config.oauth.infra.kakao.dto.LoginTokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("oauth2")
@RestController
public class OauthController {

    private final OauthService oauthService;

    @SneakyThrows // 명시되어 있는 IO Exception을 처리
    @GetMapping("/authorization/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
        ){
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/callback/{oauthServerType}")
    ResponseEntity<LoginTokenResponse> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam("code") String code
    ){
        LoginTokenResponse loginTokenResponse = oauthService.login(oauthServerType, code);
        return ResponseEntity.ok(loginTokenResponse);
    }
}
