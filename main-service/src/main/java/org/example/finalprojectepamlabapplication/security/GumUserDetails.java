package org.example.finalprojectepamlabapplication.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.finalprojectepamlabapplication.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
public class GumUserDetails implements UserDetails {

    private String username;
    private String password;
    private List<String> roles;

    public GumUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = new ArrayList<>();
        roles.add(getUserRole(user));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    private String getUserRole(User user){
        if(user.getTrainee() != null){
            return "TRAINEE";
        } else if(user.getTrainer() != null){
            return "TRAINER";
        } else {
            return "UNAUTHORIZED";
        }
    }
}
