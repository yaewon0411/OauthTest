package com.my.oauthtest.infra.oauth.client.common;

import com.my.oauthtest.domain.social.Provider;
import com.my.oauthtest.infra.oauth.client.dto.TokenRespDto;
import com.my.oauthtest.infra.oauth.client.dto.google.GoogleTokenRespDto;
import com.my.oauthtest.infra.oauth.client.dto.kakao.KakaoTokenRespDto;
import com.my.oauthtest.infra.oauth.client.google.GoogleApiClient;
import com.my.oauthtest.infra.oauth.client.google.GoogleAuthClient;
import com.my.oauthtest.infra.oauth.client.google.GoogleOAuth2Client;
import com.my.oauthtest.infra.oauth.client.kakao.KakaoApiClient;
import com.my.oauthtest.infra.oauth.client.kakao.KakaoAuthClient;
import com.my.oauthtest.infra.oauth.client.kakao.KakaoOAuth2Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2ClientFactory {

    private final GoogleAuthClient googleAuthClient;
    private final GoogleApiClient googleApiClient;

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;
    private final OAuth2Properties oAuth2Properties;

    public OAuth2Client<? extends TokenRespDto> getClient(Provider provider){
        return switch (provider){
            case KAKAO -> getKakaoClient();
            case GOOGLE -> getGoogleClient();
            default -> throw new IllegalStateException("지원하지 않는 OAuth2 provider 입니다: " + provider);
        };
    }

    public OAuth2Client<GoogleTokenRespDto> getGoogleClient() {
        return new GoogleOAuth2Client(
                googleAuthClient,
                googleApiClient,
                oAuth2Properties.getProvider(Provider.GOOGLE)
        );
    }
    public OAuth2Client<KakaoTokenRespDto> getKakaoClient(){
        return new KakaoOAuth2Client(
                kakaoAuthClient,
                kakaoApiClient,
                oAuth2Properties.getProvider(Provider.KAKAO)
        );
    }


}
