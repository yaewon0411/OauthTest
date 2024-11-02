package com.my.oauthtest.config;

import com.my.oauthtest.config.jwt.JwtProvider;
import com.my.oauthtest.domain.user.User;
import com.my.oauthtest.domain.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final String TOKEN_PREFIX = "Bearer ";
    private final String HEADER = "Authorization";

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractToken(request);
        if(token == null){
            throw new IllegalStateException("유효하지 않은 토큰");
        }
        try{
            User user = userRepository.findByEmail(jwtProvider.verify(token).getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("없는 유저임"));
            request.setAttribute("userId", user.getId());
        }catch(Exception e){
            log.error("토큰 검증 중 오류 발생: {}", e.getMessage(), e);
            throw new IllegalStateException("토큰 오류 발생");
        }
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
