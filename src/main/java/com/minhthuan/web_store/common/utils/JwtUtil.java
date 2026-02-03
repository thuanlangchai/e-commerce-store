package com.minhthuan.web_store.common.utils;



import com.minhthuan.web_store.domain.store.token.TokenService;
import com.minhthuan.web_store.domain.store.user.UserRepository;
import com.minhthuan.web_store.domain.store.user.entity.Role;
import com.minhthuan.web_store.domain.store.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    @Value("${app.jwt.accessTokenExpirationMs}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${app.jwt.refreshTokenExpirationMs}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return null;
    }

    public String generateAccessToken(Long id, String username, String email, String phone, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "access")
                .claim("id", id)
                .claim("email", email)
                .claim("phone", phone)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(Long id, String username, String email, String phone, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .claim("id", id)
                .claim("email", email)
                .claim("phone", phone)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(getKey())
                .compact();
    }

    public Claims generateAllToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateAccessToken(String token) {
        String type = generateAllToken(token).get("type", String.class);
        return type.equals("access") && generateAllToken(token).getExpiration().after(new Date());
    }

    public boolean validateRefreshToken(String token) {
        String type = generateAllToken(token).get("type", String.class);
        return type.equals("refresh") && generateAllToken(token).getExpiration().after(new Date());
    }

    public String getUsernameFromToken(String token) {
        return generateAllToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return generateAllToken(token).get("role", String.class);
    }

    public String getPhoneFromToken(String token) {
        return generateAllToken(token).get("phone", String.class);
    }

    public String getEmailFromToken(String token) {
        return generateAllToken(token).get("email", String.class);
    }

    public Long getIdFromToken(String token) {
        return  generateAllToken(token).get("id", Long.class);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
