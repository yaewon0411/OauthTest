package com.my.oauthtest.domain.social;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Provider {

    GOOGLE("google"), KAKAO("kakao");
    private final String value;

    Provider(String value) {
        this.value = value;
    }


    public static Provider fromString(String str) {
        return Arrays.stream(Provider.values())
                .filter(provider -> provider.getValue().equals(str.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 provider: "+str));
    }

}
