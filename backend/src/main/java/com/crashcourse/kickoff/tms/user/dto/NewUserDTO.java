package com.crashcourse.kickoff.tms.user.dto;

import com.crashcourse.kickoff.tms.user.model.PlayerPosition;

import lombok.Getter;
import lombok.Setter;

// Data Transfer Object to facilitate setting user roles on server side (allows for HTTP post requests with only username and password)
@Setter
@Getter
public class NewUserDTO {
    private String username;
    private String password;
    private PlayerPosition preferredPosition; // Captures the player's preferred position
}

