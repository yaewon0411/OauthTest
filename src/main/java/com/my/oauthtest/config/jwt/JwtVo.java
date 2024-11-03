package com.my.oauthtest.config.jwt;

public interface JwtVo {

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

    public static final String TOKEN_PREFIX = "Bearer ";
}
