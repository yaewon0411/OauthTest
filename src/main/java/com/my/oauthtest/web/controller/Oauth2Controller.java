package com.my.oauthtest.web.controller;

import com.my.oauthtest.web.dto.user.LoginRespDto;
import com.my.oauthtest.web.service.Oauth2Service;
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

    @GetMapping("/oauth2/authorization/{provider}")
    public void socialLogin(@PathVariable(value = "provider")String provider, HttpServletResponse response) throws IOException {
        String authorizationUrl = oauth2Service.getAuthorizationUrl(provider);
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/login/oauth2/code/{provider}")
    public ResponseEntity<LoginRespDto> callback(@PathVariable(value = "provider")String provider, @RequestParam(value = "code")String code){
        return new ResponseEntity<>(oauth2Service.processOAuth2Callback(provider, code), HttpStatus.OK);
    }


}
