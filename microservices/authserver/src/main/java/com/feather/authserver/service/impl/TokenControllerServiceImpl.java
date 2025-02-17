package com.feather.authserver.service.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.feather.authserver.config.user.UserDetailsImpl;
import com.feather.authserver.service.TokenControllerService;
import com.feather.authserver.service.TokenService;
import com.feather.lib.constants.CookieConstants;
import com.feather.lib.dto.token.LoginRequestDto;
import com.feather.lib.dto.token.LoginResponseDto;
import com.feather.lib.dto.token.RefreshResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenControllerServiceImpl implements TokenControllerService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public ResponseEntity<LoginResponseDto> processLogin(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password()));

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

        String refreshToken = tokenService.createRefreshToken(userDetailsImpl);
        String accessToken = tokenService.createAccessTokenFromRefreshToken(refreshToken, userDetailsImpl);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE,
                generateHttpOnlyCookie(CookieConstants.FEATHER_TOKEN, refreshToken,
                        tokenService.extractTokenExpiration(refreshToken)).toString());
        return ResponseEntity.ok().headers(headers).body(new LoginResponseDto(accessToken));
    }

    @Override
    public ResponseEntity<RefreshResponseDto> processRefresh(String refreshToken) {
        String username = tokenService.extractTokenSubject(refreshToken);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        String newRefreshToken = tokenService.createRefreshToken(userDetailsImpl);
        String accessToken = tokenService.createAccessTokenFromRefreshToken(refreshToken, userDetailsImpl);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE,
                generateHttpOnlyCookie(CookieConstants.FEATHER_TOKEN, newRefreshToken,
                        tokenService.extractTokenExpiration(newRefreshToken)).toString());
        return ResponseEntity.ok().headers(headers).body(new RefreshResponseDto(accessToken));

    }

    private ResponseCookie generateHttpOnlyCookie(String key, String value, long expiration) {
        return ResponseCookie
                .from(key, value)
                .path("/")
                .secure(false)
                .httpOnly(true)
                .sameSite("Strict")
                .maxAge(expiration)
                .build();

    }

}
