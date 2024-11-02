package com.my.oauthtest.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.my.oauthtest.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final String secretKey;
    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

    private static final String TOKEN_PREFIX = "Bearer ";

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }
    public String create(User user){
        String jwtToken = JWT.create()
                .withSubject("Oauth2-test: jwt")
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .withClaim("email", user.getEmail())
                .withClaim("provider", user.getProvider().toString())
                .sign(Algorithm.HMAC256(secretKey));

        return TOKEN_PREFIX + jwtToken;
    }

    public User verify(String token){
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        } catch (TokenExpiredException e){
            log.error("토큰이 만료되어 더 이상 유효하지 않습니다", e);
            throw e;
        } catch(SignatureVerificationException e){
            log.error("유효하지 않은 토큰 서명입니다", e);
            throw e;
        } catch (JWTDecodeException e) {
            log.error("유효하지 않은 JWT 형식입니다", e);
            throw e;
        }
        catch(JWTVerificationException e){
            log.error("JWT 검증 중 오류가 발생했습니다", e);
            throw e;
        }
        String email = decodedJWT.getClaim("email").asString();
        return User.builder().email(email).build();
    }

}
