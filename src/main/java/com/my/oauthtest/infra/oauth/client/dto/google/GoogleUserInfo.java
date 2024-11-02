package com.my.oauthtest.infra.oauth.client.dto.google;

import com.my.oauthtest.infra.oauth.client.common.OAuth2UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo {
    private String id;
    private String email;
    private String name;

    @Override
    public String getProviderId() {
        return id;
    }
}
