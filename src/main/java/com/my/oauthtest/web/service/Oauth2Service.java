package com.my.oauthtest.web.service;

import com.my.oauthtest.domain.user.oauth.Provider;
import com.my.oauthtest.infra.oauth.client.dto.TokenRespDto;
import com.my.oauthtest.infra.oauth.client.common.*;
import com.my.oauthtest.web.dto.user.LoginRespDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class Oauth2Service {

    private final OAuth2Properties oAuth2Properties;
    private final OAuth2ClientFactory oAuth2ClientFactory;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //인증 url 생성
    public String getAuthorizationUrl(String provider){
        OAuth2Provider oAuth2Provider = oAuth2Properties.getProvider(provider);

        return UriComponentsBuilder
                .fromUriString(oAuth2Provider.getAuthorizationUri())
                .queryParam("client_id", oAuth2Provider.getClientId())
                .queryParam("redirect_uri", oAuth2Provider.getRedirectUri())
                .queryParam("response_type","code")
                .queryParam("scope", String.join(" ", oAuth2Provider.getScopes()))
                .build()
                .toUriString();
    }

    public LoginRespDto processOAuth2Callback(String provider, String code){
        OAuth2Client<? extends TokenRespDto> client = oAuth2ClientFactory.getClient(provider);
        try{
            TokenRespDto tokenRespDto = client.getAccessToken(code);
            OAuth2UserInfo userInfo = client.getUserInfo(tokenRespDto.getAccessToken());

            return userService.login(userInfo, Provider.valueOf(provider.toUpperCase()));
        }catch (FeignException e){
            log.error("OAuth2 프로세스 실패 - provider: {}, error: {}", provider, e.getMessage(), e);
            throw new IllegalStateException("OAuth2 콜백 실패: "+e.getMessage());
        }
    }

}
