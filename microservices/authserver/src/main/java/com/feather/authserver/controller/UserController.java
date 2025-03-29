package com.feather.authserver.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.feather.lib.dto.user.CreateUserDto;
import com.feather.lib.dto.user.ResponseUserDto;
import com.feather.lib.dto.user.UpdateUserDto;

public interface UserController {

    ResponseEntity<ResponseUserDto> createUser(CreateUserDto createUserDto);

    ResponseEntity<ResponseUserDto> getUser(UUID userId);

    ResponseEntity<ResponseUserDto> updateUser(UUID userId, UpdateUserDto updateUserDto);

    ResponseEntity<Void> deleteUser(UUID userId);

}
