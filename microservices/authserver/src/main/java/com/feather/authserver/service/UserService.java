package com.feather.authserver.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.feather.authserver.dto.CreateUserDto;
import com.feather.authserver.model.User;

public interface UserService {
    Collection<? extends GrantedAuthority> loadAuthorities(User user);

    User loadUserByUsernameOrEmail(String usernameOrEmail);

    User createUser(CreateUserDto createUserDto);

    User createAdminUser(CreateUserDto createUserDto);

    User createStaffUser(CreateUserDto createUserDto);

}
