package com.my.oauthtest.web.controller;
import com.my.oauthtest.web.dto.user.UserInfoRespDto;
import com.my.oauthtest.web.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<UserInfoRespDto> getUserInfo(HttpServletRequest request){
        String provider = request.getAttribute("provider").toString();
        String providerId = request.getAttribute("providerId").toString();
        return new ResponseEntity<>(userService.getUserInfo(provider, providerId), HttpStatus.OK);
    }
}
