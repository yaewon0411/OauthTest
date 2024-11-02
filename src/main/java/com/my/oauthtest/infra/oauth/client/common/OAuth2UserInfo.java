package com.my.oauthtest.infra.oauth.client.common;

public interface OAuth2UserInfo {
    String getProviderId();
    String getEmail();
    String getName();
}
