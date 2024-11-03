package com.my.oauthtest.web.controller;

import com.my.oauthtest.web.dto.user.LinkageTokenRespDto;
import com.my.oauthtest.web.dto.user.LoginRespDto;
import com.my.oauthtest.web.service.AccountLinkageService;
import com.my.oauthtest.web.service.Oauth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class Oauth2Controller {

    private final Oauth2Service oauth2Service;
    private final AccountLinkageService linkageService;

    //1. 계정 연동 시작 -> 임시 토큰 생성
    @PostMapping("/api/account/link")
    public ResponseEntity<LinkageTokenRespDto> startAccountLinkage(@RequestParam(value = "provider")String provider, HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        return new ResponseEntity<>(linkageService.createLinkageToken(userId), HttpStatus.OK);
    }


    //2. 소셜 로그인 시작
    @GetMapping("/oauth2/authorization/{provider}")
    public void socialLogin(@PathVariable(value = "provider")String provider,
                            @RequestParam(required = false, value = "linkageToken") String linkageToken,
                            HttpServletResponse response) throws IOException {
        String authorizationUrl = oauth2Service.getAuthorizationUrl(provider, linkageToken);
        response.sendRedirect(authorizationUrl);
    }

    //3. 소셜 로그인 콜백
    @GetMapping("/login/oauth2/code/{provider}")
    public ResponseEntity<LoginRespDto> callback(@PathVariable(value = "provider")String provider,
                                                 @RequestParam(value = "code")String code,
                                                 @RequestParam(required = false, value = "state")String state){
        if(state != null){ //계정 연동 모드
            return new ResponseEntity<>(oauth2Service.processAccountLinkage(provider, code, state), HttpStatus.OK);
        }
        //일반 로그인 모드
        return new ResponseEntity<>(oauth2Service.processOAuth2Callback(provider, code), HttpStatus.OK);
    }


}
