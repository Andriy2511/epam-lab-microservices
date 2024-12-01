package org.example.finalprojectepamlabapplication.service.implementation;

import org.example.finalprojectepamlabapplication.service.BlackListService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LoginAttemptServiceImplTest {

    @Mock
    private BlackListService blackListService;

    @InjectMocks
    private LoginAttemptServiceImpl loginAttemptService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginFailed_NotBlocked() {
        String username = "testUser";

        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        Assertions.assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    public void testLoginFailed_Blocked() {
        String username = "testUser";

        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        Assertions.assertTrue(loginAttemptService.isBlocked(username));
    }

    @Test
    public void testLoginSucceeded() {
        String username = "testUser";

        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginSucceeded(username);

        Assertions.assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    public void testLogout() {
        String token = "sampleToken";

        loginAttemptService.logout(token);

        verify(blackListService, times(1)).addToBlacklist(token);
    }
}
