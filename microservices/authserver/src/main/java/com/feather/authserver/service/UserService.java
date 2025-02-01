package com.feather.authserver.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public interface UserService {
    Collection<? extends GrantedAuthority> loadAuthorities(String userId);
}
