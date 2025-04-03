package com.feather.authserver.service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;

import com.feather.authserver.dto.user.CreateUserDto;
import com.feather.authserver.dto.user.ResponseUserDto;
import com.feather.authserver.dto.user.UpdateUserDto;
import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.model.User;

public interface UserService {
    Collection<? extends GrantedAuthority> loadAuthorities(User user);

    User loadUserByUsernameOrEmail(String usernameOrEmail);

    ResponseEntity<ResponseUserDto> createUser(CreateUserDto createUserDto, FeatherRole role);

    ResponseEntity<ResponseUserDto> getUser(UUID userId);

    ResponseEntity<ResponseUserDto> updateUser(UUID userId, UpdateUserDto updateUserDto);

    void deleteUser(UUID userId);

    public User loadUser(UUID userId);

}
