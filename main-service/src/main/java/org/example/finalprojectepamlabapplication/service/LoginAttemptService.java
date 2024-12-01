package org.example.finalprojectepamlabapplication.service;

public interface LoginAttemptService {
    void loginFailed(String username);
    void loginSucceeded(String username);
    boolean isBlocked(String username);
    void logout(String token);
}
