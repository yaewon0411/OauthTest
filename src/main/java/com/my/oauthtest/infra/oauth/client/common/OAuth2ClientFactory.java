package com.my.oauthtest.infra.oauth.client.common;

import com.my.oauthtest.domain.social.Provider;
import com.my.oauthtest.infra.oauth.client.dto.TokenRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OAuth2ClientFactory {

    private final Map<String, OAuth2Client<? extends TokenRespDto>> clientMap;

    public OAuth2Client<? extends TokenRespDto> getClient(Provider provider){
        String clientBean = provider.getValue().toLowerCase() + "OAuth2Client";
        return Optional.ofNullable(clientMap.get(clientBean))
                .orElseThrow(() -> new IllegalStateException("지원하지 않는 OAuth2 provider 입니다: " + provider));
    }

}
