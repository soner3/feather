package com.feather.authserver.service;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;

import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.model.User;
import com.feather.lib.dto.user.CreateUserDto;
import com.feather.lib.dto.user.ResponseUserDto;

public interface UserService {
    Collection<? extends GrantedAuthority> loadAuthorities(User user);

    User loadUserByUsernameOrEmail(String usernameOrEmail);

    public ResponseEntity<ResponseUserDto> createUser(CreateUserDto createUserDto, FeatherRole role);

}
