package org.example.finalprojectepamlabapplication.security;

import org.example.finalprojectepamlabapplication.model.Trainee;
import org.example.finalprojectepamlabapplication.model.Trainer;
import org.example.finalprojectepamlabapplication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GumUserDetailsTest {

    private User mockUser;
    private GumUserDetails gumUserDetails;

    @BeforeEach
    void setUp() {
        mockUser = Mockito.mock(User.class);
    }

    @Test
    void testAuthoritiesForTrainee() {
        Mockito.when(mockUser.getUsername()).thenReturn("trainee_user");
        Mockito.when(mockUser.getPassword()).thenReturn("password123");
        Mockito.when(mockUser.getTrainee()).thenReturn(new Trainee());
        Mockito.when(mockUser.getTrainer()).thenReturn(null);

        gumUserDetails = new GumUserDetails(mockUser);

        Collection<? extends GrantedAuthority> authorities = gumUserDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TRAINEE")));
    }

    @Test
    void testAuthoritiesForTrainer() {
        Mockito.when(mockUser.getUsername()).thenReturn("trainer_user");
        Mockito.when(mockUser.getPassword()).thenReturn("password456");
        Mockito.when(mockUser.getTrainee()).thenReturn(null);
        Mockito.when(mockUser.getTrainer()).thenReturn(new Trainer()); // Mock Trainer object

        gumUserDetails = new GumUserDetails(mockUser);

        Collection<? extends GrantedAuthority> authorities = gumUserDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TRAINER")));
    }

    @Test
    void testAuthoritiesForUnauthorized() {
        Mockito.when(mockUser.getUsername()).thenReturn("unauth_user");
        Mockito.when(mockUser.getPassword()).thenReturn("password789");
        Mockito.when(mockUser.getTrainee()).thenReturn(null);
        Mockito.when(mockUser.getTrainer()).thenReturn(null);

        gumUserDetails = new GumUserDetails(mockUser);

        Collection<? extends GrantedAuthority> authorities = gumUserDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_UNAUTHORIZED")));
    }

    @Test
    void testGetUsername() {
        Mockito.when(mockUser.getUsername()).thenReturn("test_user");

        gumUserDetails = new GumUserDetails(mockUser);

        assertEquals("test_user", gumUserDetails.getUsername());
    }

    @Test
    void testGetPassword() {
        Mockito.when(mockUser.getPassword()).thenReturn("secure_password");

        gumUserDetails = new GumUserDetails(mockUser);

        assertEquals("secure_password", gumUserDetails.getPassword());
    }
}
