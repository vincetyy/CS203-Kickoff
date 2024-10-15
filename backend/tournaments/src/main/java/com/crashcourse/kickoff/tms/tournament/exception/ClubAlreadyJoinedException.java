package com.crashcourse.kickoff.tms.tournament.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a club attempts to join a tournament it has already joined.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ClubAlreadyJoinedException extends RuntimeException {
    public ClubAlreadyJoinedException(String message) {
        super(message);
    }
}