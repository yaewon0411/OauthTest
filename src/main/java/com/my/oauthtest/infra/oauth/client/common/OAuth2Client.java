package com.my.oauthtest.infra.oauth.client.common;

public interface OAuth2Client<T> {
    T getAccessToken(String code);
    OAuth2UserInfo getUserInfo(String accessToken);
}
