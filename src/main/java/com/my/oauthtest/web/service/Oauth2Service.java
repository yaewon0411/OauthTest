package com.my.oauthtest.web.service;

import com.my.oauthtest.domain.social.Provider;
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
    private final AccountLinkageService linkageService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String RESPONSE_TYPE = "code";

    //인증 url 생성
    public String getAuthorizationUrl(String provider, String linkageToken){
        OAuth2Provider oAuth2Provider = oAuth2Properties.getProvider(provider);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(oAuth2Provider.getAuthorizationUri())
                .queryParam("client_id", oAuth2Provider.getClientId())
                .queryParam("redirect_uri", oAuth2Provider.getRedirectUri())
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("scope", String.join(" ", oAuth2Provider.getScopes()));

        if(linkageToken != null){
            builder.queryParam("state", linkageToken);
        }

        return builder.build().toUriString();
    }

    //일반 로그인 처리
    public LoginRespDto processOAuth2Callback(String provider, String code){
        OAuth2Client<? extends TokenRespDto> client = oAuth2ClientFactory.getClient(provider);
        try{
            TokenRespDto tokenRespDto = client.getAccessToken(code);
            OAuth2UserInfo userInfo = client.getUserInfo(tokenRespDto.getAccessToken());

            return userService.login(userInfo, Provider.fromString(provider));
        }catch (FeignException e){
            log.error("OAuth2 프로세스 실패 - provider: {}, error: {}", provider, e.getMessage());
            throw new IllegalStateException("OAuth2 콜백 실패: {}"+e.getMessage(), e);
        }
    }

    //계정 연동 처리
    public LoginRespDto processAccountLinkage(String provider, String code, String linkageToken){
        try {
            //토큰으로 기존 사용자 확인
            Long userId = linkageService.getUserIdFromToken(linkageToken);

            //oauth2 인증 처리
            OAuth2Client<? extends TokenRespDto> client = oAuth2ClientFactory.getClient(provider);
            TokenRespDto tokenRespDto = client.getAccessToken(code);
            OAuth2UserInfo userInfo = client.getUserInfo(tokenRespDto.getAccessToken());

            //계정 연동 처리
            return userService.linkAccount(userId, userInfo, Provider.fromString(provider));
        } catch (FeignException e) {
            log.error("OAuth2 계정 연동 프로세스 실패 - provider: {}, linkageToken: {}, error: {}",
                    provider, linkageToken, e.getMessage());
            throw new IllegalStateException("OAuth2 계정 연동 실패: {}" + e.getMessage(), e);
        }
    }

}
