package com.bibliotheque.exception;

public class QuotaAtteintException extends RuntimeException {
    public QuotaAtteintException(String message) {
        super(message);
    }
}