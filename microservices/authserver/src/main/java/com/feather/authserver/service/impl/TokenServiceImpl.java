package com.feather.authserver.service.impl;

import java.time.Duration;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.feather.authserver.config.user.UserDetailsImpl;
import com.feather.authserver.exception.IllegalJwtTokenTypeException;
import com.feather.authserver.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private static final String TOKEN_TYPE = "TOKEN_TYPE";
    private static final String BEARER = "Bearer ";

    public enum TokenType {
        ACCESS_TOKEN,
        REFRESH_TOKEN
    }

    @Override
    public String createAccessTokenFromRefreshToken(String refreshToken, UserDetailsImpl userDetailsImpl) {
        validateRefreshToken(refreshToken);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:9000")
                .issuedAt(Instant.now())
                .claims(
                        claimsConfig -> {
                            claimsConfig.putAll(
                                    OidcUserInfo
                                            .builder()
                                            .subject(userDetailsImpl.getUserId())
                                            .email(userDetailsImpl.getEmail())
                                            .phoneNumber(userDetailsImpl.getPhoneNumber())
                                            .preferredUsername(userDetailsImpl.getUsername())
                                            .givenName(userDetailsImpl.getFirstName())
                                            .familyName(userDetailsImpl.getLastName())
                                            .build()
                                            .getClaims());
                            claimsConfig.put("roles", userDetailsImpl.getAuthorities());
                            claimsConfig.put(TOKEN_TYPE, TokenType.ACCESS_TOKEN);
                        })
                .expiresAt(Instant.now().plus(Duration.ofMinutes(1)))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public String createRefreshToken(UserDetailsImpl userDetailsImpl) {

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:9000")
                .issuedAt(Instant.now())
                .subject(userDetailsImpl.getUsername())
                .expiresAt(Instant.now().plus(Duration.ofMinutes(2)))
                .claim(TOKEN_TYPE, TokenType.REFRESH_TOKEN)
                .build();

        Jwt jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(claims));
        return jwt.getTokenValue();

    }

    @Override
    public Jwt validateAccessToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        String tokenType = jwt.getClaim(TOKEN_TYPE);
        if (!TokenType.ACCESS_TOKEN.toString().equals(tokenType)) {
            throw new IllegalJwtTokenTypeException("Invalid token type");
        }
        return jwt;

    }

    @Override
    public Jwt validateRefreshToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        String tokenType = jwt.getClaim(TOKEN_TYPE);
        if (!TokenType.REFRESH_TOKEN.toString().equals(tokenType)) {
            throw new IllegalJwtTokenTypeException("Invalid refresh token type");
        }
        return jwt;
    }

    @Override
    public String extractTokenFromHeader(HttpServletRequest httpServletRequest) {
        String maybeToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (maybeToken != null) {
            return maybeToken.substring(BEARER.length());
        }
        return null;
    }

    @Override
    public long extractTokenExpiration(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        Instant expiresAt = jwt.getExpiresAt();
        long secondsLeft = Duration.between(Instant.now(), expiresAt).getSeconds();
        return secondsLeft > 0 ? secondsLeft : 0;
    }

}
