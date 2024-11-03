package com.my.oauthtest.domain.social;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Provider {

    GOOGLE, KAKAO;

    public static Provider fromString(String str) {
        try {
            return Provider.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 provider: " + str);
        }
    }

}
