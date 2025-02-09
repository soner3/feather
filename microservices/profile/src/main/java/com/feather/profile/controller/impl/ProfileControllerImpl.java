package com.feather.profile.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feather.lib.dto.user.CreateUserDto;
import com.feather.lib.dto.user.ResponseUserDto;
import com.feather.profile.controller.ProfileController;
import com.feather.profile.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/profile")
public class ProfileControllerImpl implements ProfileController {

    private final ProfileService profileService;

    @Override
    @PostMapping("/public")
    public ResponseEntity<ResponseUserDto> registerUser(@Valid CreateUserDto createUserDto) {
        return profileService.registerUser(createUserDto);
    }

}
