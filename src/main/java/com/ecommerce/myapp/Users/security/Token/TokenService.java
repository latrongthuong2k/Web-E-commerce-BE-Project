package com.ecommerce.myapp.Users.security.Token;

import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void deleteAllExpiredTokenWhenReachedLimit(int limit) {

        int totalTokens = tokenRepository.findAllInValidToken();
        if (totalTokens > limit) {
            tokenRepository.deleteExpiredTokens();
        }

    }
}
