package com.my.oauthtest.infra.oauth.client.common.provider;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ProviderClients<A,B> {
    private A authClient;
    private B apiClient;
}
