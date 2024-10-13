package com.crashcourse.kickoff.tms.club.exception;
public class ClubAlreadyExistsException extends RuntimeException {
    public ClubAlreadyExistsException(String message) {
        super(message);
    }
}