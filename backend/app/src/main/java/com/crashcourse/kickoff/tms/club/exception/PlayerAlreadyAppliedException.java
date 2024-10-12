package com.crashcourse.kickoff.tms.club.exception;

public class PlayerAlreadyAppliedException extends RuntimeException {
    public PlayerAlreadyAppliedException(String message) {
        super(message);
    }
}
