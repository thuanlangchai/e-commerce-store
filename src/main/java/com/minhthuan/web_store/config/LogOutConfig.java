package com.minhthuan.web_store.config;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.token.TokenRepository;
import com.minhthuan.web_store.domain.store.token.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@RequiredArgsConstructor
public class LogOutConfig implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = jwtUtil.getTokenFromRequest(request);
        if (accessToken != null) {
            Token token = tokenRepository.findByToken(accessToken)
                    .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));
            token.setRevoked(true);
            token.setExpired(true);
            tokenRepository.save(token);
        }
    }
}
