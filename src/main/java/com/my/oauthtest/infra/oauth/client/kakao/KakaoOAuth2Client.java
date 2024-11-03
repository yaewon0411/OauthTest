package com.my.oauthtest.infra.oauth.client.kakao;

import com.my.oauthtest.infra.oauth.client.common.OAuth2Client;
import com.my.oauthtest.infra.oauth.client.common.OAuth2UserInfo;
import com.my.oauthtest.infra.oauth.client.dto.kakao.KakaoTokenRespDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoOAuth2Client implements OAuth2Client<KakaoTokenRespDto> {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;

    @Override
    public KakaoTokenRespDto getAccessToken(String code) {
        return null;
    }

    @Override
    public OAuth2UserInfo getUserInfo(String accessToken) {
        return null;
    }
}
