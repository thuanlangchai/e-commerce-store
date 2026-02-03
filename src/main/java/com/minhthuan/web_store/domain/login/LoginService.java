package com.minhthuan.web_store.domain.login;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.login.dto.GoogleLoginRequest;
import com.minhthuan.web_store.domain.login.dto.LoginRequest;
import com.minhthuan.web_store.domain.login.dto.LoginResponse;
import com.minhthuan.web_store.domain.login_google.GoogleTokenVerifier;
import com.minhthuan.web_store.domain.login_google.GoogleUserInfo;
import com.minhthuan.web_store.domain.store.cart.CartRepository;
import com.minhthuan.web_store.domain.store.cart.entity.Cart;
import com.minhthuan.web_store.domain.store.token.TokenService;
import com.minhthuan.web_store.domain.store.token.entity.Token;
import com.minhthuan.web_store.domain.store.user.UserRepository;
import com.minhthuan.web_store.domain.store.user.entity.Role;
import com.minhthuan.web_store.domain.store.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public ApiResponse<LoginResponse> login(LoginRequest loginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new CustomException(ErrorCode.INCORRECT_EMAIL_PASSWORD);
        }

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        tokenService.revokeAllTokenByUser(user.getId());
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getRole());
        tokenService.createToken(Token.builder()
                        .token(accessToken)
                        .isExpired(false)
                        .isRevoked(false)
                        .user(user)
                        .build());
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return new ApiResponse<>(loginResponse, "Login Successfully!");
    }

    @Transactional
    public ApiResponse<LoginResponse> loginWithGoogle(GoogleLoginRequest googleLoginRequest) {

        GoogleUserInfo googleUserInfo = googleTokenVerifier.verifyToken(googleLoginRequest.getToken());
        
        if (googleUserInfo == null) {
            throw new CustomException(ErrorCode.INCORRECT_EMAIL_PASSWORD);
        }
        

        if (!googleUserInfo.isEmailVerified()) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        

        User user = userRepository.findByEmail(googleUserInfo.getEmail())
                .orElse(null);
        

        if (user == null) {
            user = createUserFromGoogleInfo(googleUserInfo);
        }
        

        tokenService.revokeAllTokenByUser(user.getId());
        

        String accessToken = jwtUtil.generateAccessToken(
                user.getId(), 
                user.getUsername(), 
                user.getEmail(), 
                user.getPhone(), 
                user.getRole()
        );
        String refreshToken = jwtUtil.generateRefreshToken(
                user.getId(), 
                user.getUsername(), 
                user.getEmail(), 
                user.getPhone(), 
                user.getRole()
        );
        

        tokenService.createToken(Token.builder()
                .token(accessToken)
                .isExpired(false)
                .isRevoked(false)
                .user(user)
                .build());
        

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return new ApiResponse<>(loginResponse, "Login with Google successfully!");
    }
    

    private User createUserFromGoogleInfo(GoogleUserInfo googleUserInfo) {
        String username = googleUserInfo.getName() != null && !googleUserInfo.getName().isEmpty()
                ? googleUserInfo.getName()
                : googleUserInfo.getEmail().split("@")[0];

        String randomPassword = generateRandomPassword();
        

        User user = User.builder()
                .email(googleUserInfo.getEmail())
                .username(username)
                .password(passwordEncoder.encode(randomPassword)) // Hash password random
                .role(Role.USER)
                .phone(null)
                .build();
        

        user = userRepository.save(user);
        

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
        
        return user;
    }

    private String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
