package com.my.oauthtest.web.service;

import com.my.oauthtest.config.jwt.JwtProvider;
import com.my.oauthtest.domain.social.Provider;
import com.my.oauthtest.domain.social.SocialUser;
import com.my.oauthtest.domain.social.SocialUserRepository;
import com.my.oauthtest.domain.user.User;
import com.my.oauthtest.domain.user.UserRepository;
import com.my.oauthtest.infra.oauth.client.common.OAuth2UserInfo;
import com.my.oauthtest.web.dto.user.LoginRespDto;
import com.my.oauthtest.web.dto.user.UserInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final SocialUserRepository socialUserRepository;
    private final JwtProvider jwtProvider;

    /*
    * 만약 해당 provider로 등록된 유저 계정이 있다면 -> user 바로 가져오기
    * 만약 해당 provider로 등록된 유저 계정이 없다면
    * 1) 아예 유저 정보가 없는 경우 -> user 생성 후 해당 user의 socialUser 생성
    * 2) 유저 정보가 있지만, 해당 provider 연동은 안되어있는 경우 -> user 가져온 후 해당 user의 socialUser 생성
    *
    * */
    public LoginRespDto login(OAuth2UserInfo userInfo, Provider provider){



        //기존에 해당 provider 소셜 계정이 있는지 확인
        Optional<SocialUser> socialUserOP = socialUserRepository.findByProviderAndProviderId(provider, userInfo.getProviderId());

        //있다면
        if(socialUserOP.isPresent()){
            return createLoginResp(socialUserOP.get());
        }
        //없다면 -> 새 계정 생성
        User user = User.builder()
                .primaryEmail(userInfo.getEmail())
                .primaryName(userInfo.getName())
                .build();
        userRepository.save(user);
        SocialUser socialUser = SocialUser.builder()
                .user(user)
                .provider(provider)
                .providerId(userInfo.getProviderId())
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .build();
        socialUserRepository.save(socialUser);

        return createLoginResp(socialUser);
    }

    private LoginRespDto createLoginResp(SocialUser socialUserPS){
        String accessToken = jwtProvider.create(socialUserPS);
        return LoginRespDto.builder()
                .accessToken(accessToken)
                .email(socialUserPS.getEmail())
                .name(socialUserPS.getName())
                .build();
    }


    public UserInfoRespDto getUserInfo(String provider, String providerId) {
        return socialUserRepository.findByProviderAndProviderId(Provider.fromString(provider), providerId)
                .map(UserInfoRespDto::new)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저"));
    }

    public LoginRespDto linkAccount(Long userId, OAuth2UserInfo userInfo, Provider provider) {
        //기존 사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저"));

        //이미 연동된 소셜 계정인지 확인
        if(!socialUserRepository.existsByProviderAndProviderId(provider, userInfo.getProviderId())){
            throw new IllegalStateException("이미 연동된 소셜 계정입니다");
        }

        //새로운 소셜 계정 연동
        SocialUser socialUser = SocialUser.builder()
                .user(user)
                .providerId(userInfo.getProviderId())
                .provider(provider)
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .build();
        socialUserRepository.save(socialUser);

        return createLoginResp(socialUser);
    }
}
