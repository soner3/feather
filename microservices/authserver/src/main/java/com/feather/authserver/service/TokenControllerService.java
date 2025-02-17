package com.feather.authserver.service;

import org.springframework.http.ResponseEntity;

import com.feather.lib.dto.token.LoginRequestDto;
import com.feather.lib.dto.token.LoginResponseDto;
import com.feather.lib.dto.token.RefreshResponseDto;

public interface TokenControllerService {

    public ResponseEntity<LoginResponseDto> processLogin(LoginRequestDto loginRequestDto);

    public ResponseEntity<RefreshResponseDto> processRefresh(String refreshToken);
}
