package com.example.kadai_002.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.kadai_002.entity.User;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();

        if (role == null || role.isBlank()) {
            role = "USER";
        }

        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return Collections.singletonList(
            new SimpleGrantedAuthority(role)
        );
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public User getUser() {
        return user;
    }
}