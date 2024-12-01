package org.example.finalprojectepamlabapplication.controller.implementation;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.JwtTokenDTO;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.LoginRequestDTO;
import org.example.finalprojectepamlabapplication.controller.LoginController;
import org.example.finalprojectepamlabapplication.exception.UnauthorizedException;
import org.example.finalprojectepamlabapplication.security.JwtTokenProvider;
import org.example.finalprojectepamlabapplication.service.LoginAttemptService;
import org.example.finalprojectepamlabapplication.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/login")
public class LoginControllerImpl implements LoginController {
    private final UserServiceImpl userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public LoginControllerImpl(UserServiceImpl userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, LoginAttemptService loginAttemptService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    @PostMapping("/authorization")
    public JwtTokenDTO createAuthToken(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();

        if (loginAttemptService.isBlocked(username)) {
            log.debug("User {} is blocked due to too many failed login attempts", username);
            throw new UnauthorizedException("User is blocked due to too many failed login attempts. Please try again later.");
        }

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

            loginAttemptService.loginSucceeded(username);
        } catch (AuthenticationException e) {
            log.debug("The user {} failed to login", username);
            loginAttemptService.loginFailed(username);
            throw new UnauthorizedException("Invalid username or password");
        }

        UserDetails userDetails = userService.loadUserByUsername(loginRequestDTO.getUsername());
        String token = jwtTokenProvider.generateToken(userDetails);
        return new JwtTokenDTO(token);
    }

    @Override
    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        loginAttemptService.logout(token);
    }
}
