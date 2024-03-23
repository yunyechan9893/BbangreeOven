package com.bbangle.bbangle.config.oauth.domain.authcode;

import com.bbangle.bbangle.config.oauth.domain.OauthServerType;
import com.bbangle.bbangle.config.oauth.infra.GoogleOauthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider{

    private static final String SCOPE = "https://www.googleapis.com/auth/analytics";

    private final GoogleOauthConfig googleOauthConfig;
    @Override
    public OauthServerType supportServer() {
        return OauthServerType.GOOGLE;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("client_id", googleOauthConfig.clientId())
                .queryParam("redirect_uri", googleOauthConfig.redirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ", googleOauthConfig.scope()))
                .toUriString();

        //https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=191791280887-ij6l3b2eav7n2eibubo3pq0msahvha04.apps.googleusercontent.com&scope=email profile&state

    }
}
