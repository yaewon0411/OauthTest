package com.my.oauthtest.infra.oauth.client.config;

import com.my.oauthtest.infra.oauth.client.common.provider.ProviderClients;
import com.my.oauthtest.infra.oauth.client.google.GoogleApiClient;
import com.my.oauthtest.infra.oauth.client.google.GoogleAuthClient;
import com.my.oauthtest.infra.oauth.client.kakao.KakaoApiClient;
import com.my.oauthtest.infra.oauth.client.kakao.KakaoAuthClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderClientsConfig {

    @Bean("googleClients")
    public ProviderClients<GoogleAuthClient, GoogleApiClient> googleClients
            (GoogleAuthClient googleAuthClient,
             GoogleApiClient googleApiClient){
        return new ProviderClients<>(googleAuthClient, googleApiClient);
    }

    @Bean("kakaoClients")
    public ProviderClients<KakaoAuthClient, KakaoApiClient> kakaoClients(
            KakaoAuthClient kakaoAuthClient,
            KakaoApiClient kakaoApiClient) {
        return new ProviderClients<>(kakaoAuthClient, kakaoApiClient);
    }
}
