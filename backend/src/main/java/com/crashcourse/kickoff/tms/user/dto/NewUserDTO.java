package com.crashcourse.kickoff.tms.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class NewUserDTO {
    private String username;
    private String email;
    private String password;
    private String[] preferredPositions; // Captures the player's preferred position
    private String role;
}
