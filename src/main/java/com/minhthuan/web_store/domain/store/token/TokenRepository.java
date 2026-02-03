package com.minhthuan.web_store.domain.store.token;

import com.minhthuan.web_store.domain.store.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllTokenByUserId(Long userId);
    Optional<Token> findByToken(String token);
}
