package com.bibliotheque.exception;

public class OuvrageIndisponibleException extends RuntimeException {
    public OuvrageIndisponibleException(String message) {
        super(message);
    }
}