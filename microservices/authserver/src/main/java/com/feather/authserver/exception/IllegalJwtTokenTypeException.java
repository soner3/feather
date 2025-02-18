package com.feather.authserver.exception;

import org.springframework.security.oauth2.jwt.JwtException;

public class IllegalJwtTokenTypeException extends JwtException {

    public IllegalJwtTokenTypeException(String message) {
        super(message);
    }

}
