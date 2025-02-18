package com.feather.authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import com.feather.lib.constants.CookieConstants;
import com.feather.lib.dto.token.LoginRequestDto;
import com.feather.lib.dto.token.LoginResponseDto;
import com.feather.lib.dto.token.RefreshResponseDto;

import jakarta.validation.Valid;

public interface TokenController {

    public ResponseEntity<Void> csrf();

    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto);

    public ResponseEntity<RefreshResponseDto> refresh(
            @CookieValue(required = true, name = CookieConstants.FEATHER_TOKEN) String refreshToken);

}
