package com.my.oauthtest.infra.oauth.client.google;

import com.my.oauthtest.infra.oauth.client.dto.google.GoogleTokenRespDto;
import com.my.oauthtest.infra.oauth.client.common.OAuth2Client;
import com.my.oauthtest.infra.oauth.client.common.OAuth2Provider;
import com.my.oauthtest.infra.oauth.client.common.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class GoogleOAuth2Client implements OAuth2Client<GoogleTokenRespDto>{
    private final GoogleAuthClient googleAuthClient;
    private final GoogleApiClient googleApiClient;
    private final OAuth2Provider oAuth2Provider;
    private static final String TOKEN_PREFIX = "Bearer ";
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public GoogleTokenRespDto getAccessToken(String code) {
        return googleAuthClient.getAccessToken(
                oAuth2Provider.getClientId(),
                oAuth2Provider.getClientSecret(),
                code,
                "authorization_code",
                oAuth2Provider.getRedirectUri());
    }

    @Override
    public OAuth2UserInfo getUserInfo(String accessToken) {
        log.info("구글 accessToken : {}", accessToken);
        return googleApiClient.getUserInfo(TOKEN_PREFIX+accessToken);
    }
}
