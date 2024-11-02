package com.my.oauthtest.web.service;

import com.my.oauthtest.config.jwt.JwtProvider;
import com.my.oauthtest.domain.user.oauth.Provider;
import com.my.oauthtest.domain.user.User;
import com.my.oauthtest.domain.user.UserRepository;
import com.my.oauthtest.infra.oauth.client.common.OAuth2UserInfo;
import com.my.oauthtest.web.dto.user.LoginRespDto;
import com.my.oauthtest.web.dto.user.UserInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public LoginRespDto login(OAuth2UserInfo userInfo, Provider provider){
        User user = userRepository.findByEmailAndProvider(userInfo.getEmail(), provider)
                .orElseGet(() -> register(userInfo, provider));

        String accessToken = jwtProvider.create(user);

        return LoginRespDto.builder()
                .accessToken(accessToken)
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }


    private User register(OAuth2UserInfo userInfo, Provider provider){
        User user = User.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .provider(provider)
                .providerId(userInfo.getProviderId())
                .build();

        return userRepository.save(user);
    }

    public UserInfoRespDto getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .map(UserInfoRespDto::new)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저"));
    }

}
