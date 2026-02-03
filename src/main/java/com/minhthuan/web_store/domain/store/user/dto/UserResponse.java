package com.minhthuan.web_store.domain.store.user.dto;


import com.minhthuan.web_store.domain.store.user.entity.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private String phone;
}
