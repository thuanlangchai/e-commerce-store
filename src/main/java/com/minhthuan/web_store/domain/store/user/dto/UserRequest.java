package com.minhthuan.web_store.domain.store.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {
    @NotBlank  @NotNull
    private String username;
    @Email
    @NotBlank  @NotNull
    private String email;
    @Size(min = 6, max = 20, message = "PASSWORD_INVALID")
    private String password;
    @Size(min = 10, max = 10, message = "PHONE_INVALID")
    private String phone;
}
