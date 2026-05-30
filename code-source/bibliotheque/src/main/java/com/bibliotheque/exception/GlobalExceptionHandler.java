package com.bibliotheque.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(QuotaAtteintException.class)
    public ResponseEntity<Map<String, Object>> handleQuotaAtteint(QuotaAtteintException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
    @ExceptionHandler(CompteSuspenduException.class)
    public ResponseEntity<Map<String, Object>> handleCompteSuspendu(CompteSuspenduException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }
    
    @ExceptionHandler(OuvrageIndisponibleException.class)
    public ResponseEntity<Map<String, Object>> handleOuvrageIndisponible(OuvrageIndisponibleException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
    
    @ExceptionHandler(DejaDansFileException.class)
    public ResponseEntity<Map<String, Object>> handleDejaDansFile(DejaDansFileException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
    
    @ExceptionHandler(ReservationExpireeException.class)
    public ResponseEntity<Map<String, Object>> handleReservationExpiree(ReservationExpireeException ex) {
        return buildResponse(HttpStatus.GONE, ex.getMessage());
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}