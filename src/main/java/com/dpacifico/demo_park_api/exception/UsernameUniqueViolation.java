package com.dpacifico.demo_park_api.exception;

public class UsernameUniqueViolation extends RuntimeException {
    public UsernameUniqueViolation(String message) {
        super(message);
    }
}
