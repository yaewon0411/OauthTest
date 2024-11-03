package com.my.oauthtest.infra.oauth.client.kakao;

import com.my.oauthtest.config.oauth.OAuth2FeignConfig;
import com.my.oauthtest.infra.oauth.client.common.OAuth2Client;
import com.my.oauthtest.infra.oauth.client.dto.kakao.KakaoTokenRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoAuthClient", url = "https://kauth.kakao.com/oauth", configuration = OAuth2FeignConfig.class)
public interface KakaoAuthClient {

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoTokenRespDto getAccessToken(@RequestParam("client_id") String clientId,
                                     @RequestParam("client_secret") String clientSecret,
                                     @RequestParam("code")String code,
                                     @RequestParam("grant_type")String grantType,
                                     @RequestParam("redirect_uri") String redirectUri);

}
