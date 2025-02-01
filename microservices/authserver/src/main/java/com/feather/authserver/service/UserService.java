package com.feather.authserver.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.feather.authserver.model.User;

public interface UserService {
    Collection<? extends GrantedAuthority> loadAuthorities(User user);

    User loadUserByUsernameOrEmail(String usernameOrEmail);

}
