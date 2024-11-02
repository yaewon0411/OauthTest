package com.my.oauthtest.web.dto.user;

import com.my.oauthtest.domain.user.User;
import com.my.oauthtest.domain.user.oauth.Provider;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserInfoRespDto{
    private String name;
    private String email;
    private Provider provider;

    public UserInfoRespDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.provider = user.getProvider();
    }
}