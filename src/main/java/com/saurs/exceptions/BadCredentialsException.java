package com.saurs.exceptions;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Bad credentials.");
    }
}
