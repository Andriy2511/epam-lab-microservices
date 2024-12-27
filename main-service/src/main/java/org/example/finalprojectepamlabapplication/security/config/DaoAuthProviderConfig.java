package org.example.finalprojectepamlabapplication.security.config;

import org.example.finalprojectepamlabapplication.service.implementation.UserDetailsLoaderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DaoAuthProviderConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsLoaderImpl userDetailsLoader;

    @Autowired
    public DaoAuthProviderConfig(PasswordEncoder passwordEncoder, UserDetailsLoaderImpl userDetailsLoader) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsLoader = userDetailsLoader;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsLoader);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
