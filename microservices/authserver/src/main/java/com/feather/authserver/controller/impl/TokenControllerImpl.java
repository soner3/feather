package com.feather.authserver.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feather.authserver.controller.TokenController;
import com.feather.authserver.service.TokenControllerService;
import com.feather.lib.constants.CookieConstants;
import com.feather.lib.dto.token.CsrfDto;
import com.feather.lib.dto.token.LoginRequestDto;
import com.feather.lib.dto.token.LoginResponseDto;
import com.feather.lib.dto.token.RefreshResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/token")
@RequiredArgsConstructor
public class TokenControllerImpl implements TokenController {

    private final TokenControllerService tokenControllerService;

    @Override
    @GetMapping("/csrf")
    public ResponseEntity<CsrfDto> csrf(CsrfToken csrfToken) {
        return ResponseEntity.ok(new CsrfDto(csrfToken.getToken()));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return tokenControllerService.processLogin(loginRequestDto);
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refresh(
            @CookieValue(required = true, name = CookieConstants.FEATHER_TOKEN) String refreshToken) {
        return tokenControllerService.processRefresh(refreshToken);
    }

}
