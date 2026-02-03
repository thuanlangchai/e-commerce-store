package com.minhthuan.web_store.domain.store.token;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.domain.store.token.TokenRepository;
import com.minhthuan.web_store.domain.store.token.entity.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public void createToken(Token token) {
        tokenRepository.save(token);
    }

    public void revokeAllTokenByUser(Long userId) {
        for (Token token : tokenRepository.findAllTokenByUserId(userId)) {
            token.setRevoked(true);
            token.setExpired(true);
            tokenRepository.save(token);
        }
    }

    public  Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public boolean isRevokedToken(String token) {
        Token token1 = tokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));
        return token1.isRevoked() || token1.isExpired();
    }

}
