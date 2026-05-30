package com.bibliotheque.exception;

public class ReservationExpireeException extends RuntimeException {
    public ReservationExpireeException(String message) {
        super(message);
    }
}