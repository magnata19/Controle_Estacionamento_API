package com.dpacifico.demo_park_api.exception;

public class CpfUniqueViolation extends RuntimeException {
    public CpfUniqueViolation(String message) {
        super(message);
    }
}
