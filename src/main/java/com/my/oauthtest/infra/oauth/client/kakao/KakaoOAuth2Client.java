package com.my.oauthtest.infra.oauth.client.kakao;

import com.my.oauthtest.config.jwt.JwtVo;
import com.my.oauthtest.infra.oauth.client.common.OAuth2Client;
import com.my.oauthtest.infra.oauth.client.common.OAuth2Provider;
import com.my.oauthtest.infra.oauth.client.common.OAuth2UserInfo;
import com.my.oauthtest.infra.oauth.client.dto.kakao.KakaoTokenRespDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class KakaoOAuth2Client implements OAuth2Client<KakaoTokenRespDto> {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;
    private final OAuth2Provider oAuth2Provider;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String GRANT_TYPE = "authorization_code";


    @Override
    public KakaoTokenRespDto getAccessToken(String code) {
        return kakaoAuthClient.getAccessToken(
                oAuth2Provider.getClientId(),
                oAuth2Provider.getClientSecret(),
                code,
                GRANT_TYPE,
                oAuth2Provider.getRedirectUri());
    }

    @Override
    public OAuth2UserInfo getUserInfo(String accessToken) {
        log.info("카카오 accessToken : {}", accessToken);
        return kakaoApiClient.getUserInfo(JwtVo.TOKEN_PREFIX+accessToken);
    }
}
