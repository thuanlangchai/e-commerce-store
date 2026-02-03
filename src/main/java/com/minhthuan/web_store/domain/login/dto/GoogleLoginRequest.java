package com.minhthuan.web_store.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO cho Google OAuth login
 * Frontend gửi Google access token đến endpoint /login/google
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginRequest {
    /**
     * Google access token từ @react-oauth/google
     * Token này được Google cấp sau khi user đăng nhập thành công
     */
    private String token;
}

