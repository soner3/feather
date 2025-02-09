package com.feather.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.feather.lib.dto.user.CreateUserDto;
import com.feather.lib.dto.user.ResponseUserDto;

import jakarta.validation.Valid;

public interface ProfileController {

    ResponseEntity<ResponseUserDto> registerUser(@Valid @RequestBody CreateUserDto createUserDto);

}
