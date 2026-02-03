package com.minhthuan.web_store.domain.login_google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa thông tin user từ Google sau khi verify token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfo {
    private String email;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
    private boolean emailVerified;
}

