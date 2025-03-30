package com.feather.authserver.exception;

public class CompromisedPasswordException extends RuntimeException {

    public CompromisedPasswordException(String msg) {
        super(msg);
    }
}
