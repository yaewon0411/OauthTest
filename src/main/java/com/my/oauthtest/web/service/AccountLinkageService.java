package com.my.oauthtest.web.service;

import com.my.oauthtest.web.dto.user.LinkageTokenRespDto;
import lombok.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AccountLinkageService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String TOKEN_PREFIX = "link:";
    private static final long TOKEN_VALID_TIME = 5; //5분으로 설정

    public LinkageTokenRespDto createLinkageToken(Long userId){
        String token = UUID.randomUUID().toString();
        String redisKey = TOKEN_PREFIX + token;

        redisTemplate.opsForValue().set(
                redisKey,
                userId.toString(),
                TOKEN_VALID_TIME,
                TimeUnit.MINUTES
        );

        return new LinkageTokenRespDto(token);
    }

    public Long getUserIdFromToken(String token){
        String redisKey = TOKEN_PREFIX + token;
        String userId = redisTemplate.opsForValue().get(redisKey);

        if(userId == null){
            throw new IllegalStateException("유효하지 않거나 만료된 연동 토큰입니다");
        }
        redisTemplate.delete(redisKey);

        return Long.parseLong(userId);
    }
}