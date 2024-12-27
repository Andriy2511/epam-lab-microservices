package org.example.finalprojectepamlabapplication.service.implementation;

import org.example.finalprojectepamlabapplication.model.User;
import org.example.finalprojectepamlabapplication.repository.UserRepository;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.example.finalprojectepamlabapplication.service.UserDetailsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsLoaderImpl implements UserDetailsLoader {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsLoaderImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new GumUserDetails(user);
    }
}
