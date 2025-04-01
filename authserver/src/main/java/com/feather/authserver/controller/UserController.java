package com.feather.authserver.controller;

import org.springframework.http.ResponseEntity;

import com.feather.authserver.config.user.UserDetailsImpl;
import com.feather.authserver.dto.user.CreateUserDto;
import com.feather.authserver.dto.user.ResponseUserDto;
import com.feather.authserver.dto.user.UpdateUserDto;

public interface UserController {

    ResponseEntity<ResponseUserDto> createUser(CreateUserDto createUserDto);

    ResponseEntity<ResponseUserDto> getUser(UserDetailsImpl userDetailsImpl);

    ResponseEntity<ResponseUserDto> updateUser(UpdateUserDto updateUserDto,
            UserDetailsImpl userDetailsImpl);

    ResponseEntity<Void> deleteUser(UserDetailsImpl userDetailsImpl);

}
