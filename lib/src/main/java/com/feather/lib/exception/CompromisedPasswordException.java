package com.feather.lib.exception;

public class CompromisedPasswordException extends RuntimeException {

    public CompromisedPasswordException(String msg) {
        super(msg);
    }
}
