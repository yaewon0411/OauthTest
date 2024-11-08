package com.my.oauthtest.infra.oauth.client.config;

import com.my.oauthtest.domain.social.Provider;
import com.my.oauthtest.infra.oauth.client.common.OAuth2Client;
import com.my.oauthtest.infra.oauth.client.common.OAuth2Properties;
import com.my.oauthtest.infra.oauth.client.common.provider.ProviderClients;
import com.my.oauthtest.infra.oauth.client.dto.google.GoogleTokenRespDto;
import com.my.oauthtest.infra.oauth.client.dto.kakao.KakaoTokenRespDto;
import com.my.oauthtest.infra.oauth.client.google.GoogleApiClient;
import com.my.oauthtest.infra.oauth.client.google.GoogleAuthClient;
import com.my.oauthtest.infra.oauth.client.google.GoogleOAuth2Client;
import com.my.oauthtest.infra.oauth.client.kakao.KakaoApiClient;
import com.my.oauthtest.infra.oauth.client.kakao.KakaoAuthClient;
import com.my.oauthtest.infra.oauth.client.kakao.KakaoOAuth2Client;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class OAuth2ClientConfig {

    private final Map<String, ProviderClients<?,?>> providerClientsMap;
    private final OAuth2Properties oAuth2Properties;

    private ProviderClients<?,?> getClients(Provider provider){
        return Optional.ofNullable(providerClientsMap.get(provider.name().toLowerCase()+"Clients"))
                .orElseThrow(() -> new IllegalStateException("제공하지 않는 Provider 입니다: " + provider));
    }

    @Bean("googleOAuth2Client")
    public OAuth2Client<GoogleTokenRespDto> getGoogleClient() {
        ProviderClients<GoogleAuthClient, GoogleApiClient> clients = (ProviderClients<GoogleAuthClient, GoogleApiClient>) getClients(Provider.GOOGLE);
        return new GoogleOAuth2Client(
                clients.getAuthClient(),
                clients.getApiClient(),
                oAuth2Properties.getProvider(Provider.GOOGLE)
        );
    }

    @Bean("kakaoOAuth2Client")
    public OAuth2Client<KakaoTokenRespDto> getKakaoClient(){
        ProviderClients<KakaoAuthClient, KakaoApiClient> clients = (ProviderClients<KakaoAuthClient, KakaoApiClient>) getClients(Provider.KAKAO);
        return new KakaoOAuth2Client(
                clients.getAuthClient(),
                clients.getApiClient(),
                oAuth2Properties.getProvider(Provider.KAKAO)
        );
    }
}
