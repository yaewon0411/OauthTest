package com.my.oauthtest.infra.oauth.client.google;

import com.my.oauthtest.config.oauth.OAuth2FeignConfig;
import com.my.oauthtest.infra.oauth.client.dto.google.GoogleTokenRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleAuthClient", url = "https://oauth2.googleapis.com", configuration = OAuth2FeignConfig.class)
public interface GoogleAuthClient {

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    GoogleTokenRespDto getAccessToken(@RequestParam("client_id") String clientId,
                                      @RequestParam("client_secret") String clientSecret,
                                      @RequestParam("code")String code,
                                      @RequestParam("grant_type")String grantType,
                                      @RequestParam("redirect_uri") String redirectUri);
}
