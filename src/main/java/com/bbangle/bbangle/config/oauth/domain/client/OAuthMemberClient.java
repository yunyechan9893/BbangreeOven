package com.bbangle.bbangle.config.oauth.domain.client;

import com.bbangle.bbangle.config.oauth.domain.OauthServerType;
import com.bbangle.bbangle.member.domain.Member;

public interface OAuthMemberClient {
    Member fetch(String code);
    OauthServerType supportServer();


}
