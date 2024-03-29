package com.bbangle.bbangle.token.oauth.domain.client;

import com.bbangle.bbangle.token.oauth.domain.OauthServerType;
import com.bbangle.bbangle.member.domain.Member;

public interface OAuthMemberClient {
    Member fetch(String code);
    OauthServerType supportServer();


}
