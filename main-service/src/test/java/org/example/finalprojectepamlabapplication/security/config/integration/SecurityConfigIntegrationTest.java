package org.example.finalprojectepamlabapplication.security.config.integration;

import org.example.finalprojectepamlabapplication.security.JwtTokenProvider;
import org.example.finalprojectepamlabapplication.security.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void whenAccessPublicEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAccessProtectedEndpointWithoutAuthThenReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/trainings"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenTraineeAccessTrainerEndpointWithTokenThenReturnForbidden() throws Exception {
        UserDetails userDetails = User.withUsername("trainee")
                .password("password")
                .roles("TRAINEE")
                .build();

        String token = jwtTokenProvider.generateToken(userDetails);

        mockMvc.perform(get("/trainers/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
