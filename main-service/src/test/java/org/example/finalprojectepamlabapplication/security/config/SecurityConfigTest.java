package org.example.finalprojectepamlabapplication.security.config;

import org.example.finalprojectepamlabapplication.security.JwtRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    public void setup() {
        securityConfig = new SecurityConfig(null);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertTrue(passwordEncoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
    }

    @Test
    public void testAuthenticationManager() throws Exception {
        AuthenticationConfiguration authenticationConfiguration = Mockito.mock(AuthenticationConfiguration.class);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);

        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);
        assertNotNull(result);
    }

    @Test
    public void testFilterChain() throws Exception {
        JwtRequestFilter jwtRequestFilter = Mockito.mock(JwtRequestFilter.class);
        securityConfig = new SecurityConfig(jwtRequestFilter);

        HttpSecurity httpSecurity = Mockito.mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.exceptionHandling(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);

        SecurityFilterChain filterChain = securityConfig.filterChain(httpSecurity);
        assertNotNull(filterChain);

        verify(httpSecurity).csrf(any());
        verify(httpSecurity).cors(any());
        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).sessionManagement(any());
        verify(httpSecurity).exceptionHandling(any());
        verify(httpSecurity).addFilterBefore(eq(jwtRequestFilter), any());
    }
}
