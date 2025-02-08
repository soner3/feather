package com.feather.profile.service;

import org.springframework.http.ResponseEntity;

import com.feather.lib.dto.user.CreateUserDto;
import com.feather.lib.dto.user.ResponseUserDto;

public interface ProfileService {

    ResponseEntity<ResponseUserDto> registerUser(CreateUserDto createUserDto);

}
