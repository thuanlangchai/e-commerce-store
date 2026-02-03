package com.minhthuan.web_store.domain.login.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
}
