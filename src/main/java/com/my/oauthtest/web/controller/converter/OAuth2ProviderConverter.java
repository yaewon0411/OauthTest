package com.my.oauthtest.web.controller.converter;

import com.my.oauthtest.domain.social.Provider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OAuth2ProviderConverter implements Converter<String, Provider> {
    @Override
    public Provider convert(String source) {
        try{
            return Provider.fromString(source);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 제공자");
        }
    }
}
