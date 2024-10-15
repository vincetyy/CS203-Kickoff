package com.crashcourse.kickoff.tms.tournament.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a club attempts to join a tournament without sufficient players
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class NotEnoughPlayersException extends RuntimeException {
    public NotEnoughPlayersException(String message) {
        super(message);
    }
}