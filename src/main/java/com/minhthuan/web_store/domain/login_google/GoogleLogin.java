package com.minhthuan.web_store.domain.login_google;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleLogin {
    @GetMapping("/login/google")
    public String googleLogin() {
        return "Google Login Endpoint";
    }
}
