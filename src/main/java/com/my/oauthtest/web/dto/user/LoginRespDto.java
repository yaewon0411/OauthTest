package com.my.oauthtest.web.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRespDto {

    private String accessToken;
    private String email;
    private String name;

    @Builder
    public LoginRespDto(String accessToken, String email, String name) {
        this.accessToken = accessToken;
        this.email = email;
        this.name = name;
    }
}
