package com.my.oauthtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OauthTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthTestApplication.class, args);
    }

}
