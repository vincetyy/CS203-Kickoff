package com.crashcourse.kickoff.tms.club.exception;
public class PlayerLimitExceededException extends RuntimeException {
    public PlayerLimitExceededException(String message) {
        super(message);
    }
}