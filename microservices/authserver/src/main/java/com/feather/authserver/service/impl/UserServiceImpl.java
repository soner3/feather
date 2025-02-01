package com.feather.authserver.service.impl;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.feather.authserver.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Collection<? extends GrantedAuthority> loadAuthorities(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadAuthorities'");
    }

}
