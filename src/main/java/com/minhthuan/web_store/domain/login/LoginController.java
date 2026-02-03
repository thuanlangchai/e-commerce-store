package com.minhthuan.web_store.domain.login;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.login.dto.GoogleLoginRequest;
import com.minhthuan.web_store.domain.login.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }


    @PostMapping("/login/google")
    public ResponseEntity<ApiResponse<?>> loginWithGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) {
        return ResponseEntity.ok(loginService.loginWithGoogle(googleLoginRequest));
    }
}
