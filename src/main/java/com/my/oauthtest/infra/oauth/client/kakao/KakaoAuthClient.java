package com.my.oauthtest.infra.oauth.client.kakao;

import com.my.oauthtest.config.oauth.OAuth2FeignConfig;
import com.my.oauthtest.infra.oauth.client.common.OAuth2Client;
import com.my.oauthtest.infra.oauth.client.dto.kakao.KakaoTokenRespDto;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "KakaoAuthClient", url = "https://kauth.kakao.com/oauth/authorize", configuration = OAuth2FeignConfig.class)
public interface KakaoAuthClient {

    KakaoTokenRespDto getAccessToken();

}
