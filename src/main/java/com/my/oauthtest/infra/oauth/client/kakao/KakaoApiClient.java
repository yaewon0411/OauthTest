package com.my.oauthtest.infra.oauth.client.kakao;

import com.my.oauthtest.config.oauth.OAuth2FeignConfig;
import com.my.oauthtest.infra.oauth.client.dto.kakao.KakaoUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com", configuration = OAuth2FeignConfig.class)
public interface KakaoApiClient {
    @GetMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoUserInfo getUserInfo(@RequestHeader("Authorization")String token);

}
