package com.my.oauthtest.infra.oauth.client.google;

import com.my.oauthtest.config.oauth.OAuth2FeignConfig;
import com.my.oauthtest.infra.oauth.client.dto.google.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleApiClient", url = "https://www.googleapis.com", configuration = OAuth2FeignConfig.class)
public interface GoogleApiClient {

    @GetMapping("/oauth2/v2/userinfo")
    GoogleUserInfo getUserInfo(@RequestHeader("Authorization") String token);



}
