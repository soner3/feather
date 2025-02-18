package com.feather.authserver.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feather.authserver.controller.TokenController;
import com.feather.authserver.service.TokenControllerService;
import com.feather.lib.constants.CookieConstants;
import com.feather.lib.dto.token.LoginRequestDto;
import com.feather.lib.dto.token.LoginResponseDto;
import com.feather.lib.dto.token.RefreshResponseDto;
import com.feather.lib.util.HttpErrorInfo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/token")
@RequiredArgsConstructor
public class TokenControllerImpl implements TokenController {

    private final TokenControllerService tokenControllerService;

    @Override
    @GetMapping("/csrf")
    public ResponseEntity<Void> csrf() {
        return ResponseEntity.noContent().build();
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

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleMissingRequestCookieException(MissingRequestCookieException ex) {
        return HttpErrorInfo.errorInfo(ex, "Missing Request Cookie", HttpStatus.FORBIDDEN);
    }

}
