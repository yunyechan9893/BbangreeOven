package com.bbangle.bbangle.config.oauth.domain;

import java.util.Locale;

import static java.util.Locale.ENGLISH;

public enum OauthServerType {
    KAKAO,GOOGLE;

    public static OauthServerType fromName(String type){
        return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
    }
}
