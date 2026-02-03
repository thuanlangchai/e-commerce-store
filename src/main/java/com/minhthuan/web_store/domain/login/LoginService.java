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

    /**
     * Đăng nhập với Google OAuth
     * 
     * Flow:
     * 1. Frontend gửi Google access token
     * 2. Verify token với Google API để lấy thông tin user
     * 3. Tìm user trong database theo email
     * 4. Nếu chưa có user -> tạo user mới (auto-register)
     * 5. Tạo JWT token và trả về như login thông thường
     * 
     * @param googleLoginRequest: Chứa Google access token từ frontend
     * @return LoginResponse với JWT tokens
     */
    @Transactional
    public ApiResponse<LoginResponse> loginWithGoogle(GoogleLoginRequest googleLoginRequest) {
        // Bước 1: Verify Google token và lấy thông tin user
        GoogleUserInfo googleUserInfo = googleTokenVerifier.verifyToken(googleLoginRequest.getToken());
        
        if (googleUserInfo == null) {
            throw new CustomException(ErrorCode.INCORRECT_EMAIL_PASSWORD);
        }
        
        // Bước 2: Kiểm tra email đã được verified chưa
        if (!googleUserInfo.isEmailVerified()) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        
        // Bước 3: Tìm user trong database theo email
        User user = userRepository.findByEmail(googleUserInfo.getEmail())
                .orElse(null);
        
        // Bước 4: Nếu chưa có user -> tạo user mới
        if (user == null) {
            user = createUserFromGoogleInfo(googleUserInfo);
        }
        
        // Bước 5: Revoke tất cả token cũ của user (security best practice)
        tokenService.revokeAllTokenByUser(user.getId());
        
        // Bước 6: Tạo JWT tokens mới
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
        
        // Bước 7: Lưu access token vào database
        tokenService.createToken(Token.builder()
                .token(accessToken)
                .isExpired(false)
                .isRevoked(false)
                .user(user)
                .build());
        
        // Bước 8: Trả về response
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return new ApiResponse<>(loginResponse, "Login with Google successfully!");
    }
    
    /**
     * Tạo user mới từ thông tin Google
     * User được tạo với:
     * - Email từ Google
     * - Username từ Google name (hoặc email nếu không có name)
     * - Password random (vì Google user không có password)
     * - Role: USER (mặc định)
     * - Phone: null (có thể cập nhật sau)
     */
    private User createUserFromGoogleInfo(GoogleUserInfo googleUserInfo) {
        // Tạo username từ Google name hoặc email
        String username = googleUserInfo.getName() != null && !googleUserInfo.getName().isEmpty()
                ? googleUserInfo.getName()
                : googleUserInfo.getEmail().split("@")[0]; // Lấy phần trước @ nếu không có name
        
        // Tạo password random cho Google user (không bao giờ dùng đến)
        // Vì User entity yêu cầu password không null
        String randomPassword = generateRandomPassword();
        
        // Tạo user mới
        User user = User.builder()
                .email(googleUserInfo.getEmail())
                .username(username)
                .password(passwordEncoder.encode(randomPassword)) // Hash password random
                .role(Role.USER)
                .phone(null) // Có thể cập nhật sau
                .build();
        
        // Lưu user vào database
        user = userRepository.save(user);
        
        // Tạo cart cho user mới
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
        
        return user;
    }
    
    /**
     * Tạo password random cho Google user
     * Password này không bao giờ được sử dụng vì user đăng nhập bằng Google
     */
    private String generateRandomPassword() {
        // Tạo password random 32 ký tự
        // Vì user đăng nhập bằng Google nên password này chỉ để thỏa mãn constraint
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
