package com.feather.authserver.service;

import com.feather.authserver.config.user.UserDetailsImpl;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {

    String createAccessTokenFromRefreshToken(String refreshToken, UserDetailsImpl userDetailsImpl);

    String createRefreshToken(UserDetailsImpl userDetailsImpl);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

    long extractTokenExpiration(String token);

    String extractTokenFromHeader(HttpServletRequest httpServletRequest);

    String extractTokenSubject(String token);

}
