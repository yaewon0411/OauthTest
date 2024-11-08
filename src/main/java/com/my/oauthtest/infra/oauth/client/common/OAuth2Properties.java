package com.my.oauthtest.infra.oauth.client.common;

import com.my.oauthtest.domain.social.Provider;
import com.my.oauthtest.infra.oauth.client.common.provider.OAuth2Provider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Properties {
    private Map<String, OAuth2Provider> providers;

    public OAuth2Provider getProvider(Provider provider){
        return providers.get(provider.getValue());
    }
}
