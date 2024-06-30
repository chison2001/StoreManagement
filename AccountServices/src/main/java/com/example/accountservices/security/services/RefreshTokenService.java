package com.example.accountservices.security.services;

import com.example.accountservices.entities.Account;
import com.example.accountservices.entities.User;
import com.example.accountservices.exception.TokenRefreshException;
import com.example.accountservices.repositories.AccountRepository;
import com.example.accountservices.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private Long refreshTokenDurationMs = 864000L ;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<Account> findByToken(String token) {
        return accountRepository.findByRefreshToken(token);
    }

    public Account createRefreshToken(String userId) {
        Account refreshToken = new Account();

        Optional<User> user = userRepository.findById(userId);

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenDurationMs).getNano());
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        refreshToken = accountRepository.save(refreshToken);
        return refreshToken;
    }

    public Account verifyExpiration(Account account) {
        if (account.getExpiresAt().compareTo(Instant.now().getNano()) < 0) {
            account.setRefreshToken(null);
            accountRepository.save(account);
            throw new TokenRefreshException(account.getRefreshToken(), "Refresh token was expired. Please make a new signin request");
        }

        return account;
    }
    @Transactional
    public int deleteByUserId(String userId) {
        return accountRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
