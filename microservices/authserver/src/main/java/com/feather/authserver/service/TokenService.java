package com.feather.authserver.service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.feather.authserver.config.user.UserDetailsImpl;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {

    String createAccessTokenFromRefreshToken(String refreshToken, UserDetailsImpl userDetailsImpl);

    String createRefreshToken(UserDetailsImpl userDetailsImpl);

    Jwt validateAccessToken(String token);

    Jwt validateRefreshToken(String token);

    long extractTokenExpiration(String token);

    String extractTokenFromHeader(HttpServletRequest httpServletRequest);

}
