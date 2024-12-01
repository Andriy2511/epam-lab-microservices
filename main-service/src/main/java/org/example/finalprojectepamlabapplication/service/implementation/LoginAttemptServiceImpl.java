package org.example.finalprojectepamlabapplication.service.implementation;

import lombok.Getter;
import lombok.Setter;
import org.example.finalprojectepamlabapplication.service.BlackListService;
import org.example.finalprojectepamlabapplication.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final BlackListService blackListService;

    private static final int MAX_ATTEMPTS = 3;
    private static final int BLOCK_TIME_DURATION = 5;

    private final Map<String, LoginAttempt> attemptsCache = new ConcurrentHashMap<>();

    @Autowired
    public LoginAttemptServiceImpl(BlackListService blackListService) {
        this.blackListService = blackListService;
    }

    @Override
    public void loginFailed(String username) {
        LoginAttempt loginAttempt = attemptsCache.getOrDefault(username, new LoginAttempt(0, null));
        loginAttempt.setAttempts(loginAttempt.getAttempts() + 1);
        loginAttempt.setLastModified(LocalDateTime.now());

        if (loginAttempt.getAttempts() >= MAX_ATTEMPTS) {
            loginAttempt.setBlockedUntil(LocalDateTime.now().plusMinutes(BLOCK_TIME_DURATION));
        }

        attemptsCache.put(username, loginAttempt);
    }

    @Override
    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
    }

    @Override
    public boolean isBlocked(String username) {
        LoginAttempt loginAttempt = attemptsCache.get(username);
        if (loginAttempt == null) {
            return false;
        }

        if (loginAttempt.getBlockedUntil() == null) {
            return false;
        } else if (loginAttempt.getBlockedUntil().isAfter(LocalDateTime.now())) {
            return true;
        } else {
            attemptsCache.remove(username);
            return false;
        }
    }

    @Override
    public void logout(String token) {
        blackListService.addToBlacklist(token);
    }

    @Getter
    @Setter
    private static class LoginAttempt {
        private int attempts;
        private LocalDateTime lastModified;
        private LocalDateTime blockedUntil;

        public LoginAttempt(int attempts, LocalDateTime blockedUntil) {
            this.attempts = attempts;
            this.blockedUntil = blockedUntil;
        }
    }
}
