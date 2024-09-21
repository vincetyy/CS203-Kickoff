package com.crashcourse.kickoff.tms.club.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ClubExceptionHandler {

    // ClubAlreadyExistsException
    @ExceptionHandler(ClubAlreadyExistsException.class)
    public ResponseEntity<?> handleClubAlreadyExistsException(ClubAlreadyExistsException ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.toString());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // ClubNotFoundException
    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<?> handleClubNotFoundException(ClubNotFoundException ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.toString());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // PlayerLimitExceededException
    @ExceptionHandler(PlayerLimitExceededException.class)
    public ResponseEntity<?> handlePlayerLimitExceededException(PlayerLimitExceededException ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "An error occurred: " + ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}