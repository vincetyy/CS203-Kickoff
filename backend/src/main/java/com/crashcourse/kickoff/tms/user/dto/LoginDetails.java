package com.crashcourse.kickoff.tms.user.dto;

import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.user.model.Role;

import lombok.Getter;
import lombok.Setter;

// Data Transfer Object to facilitate setting user roles on server side (allows for HTTP post requests with only username and password)
@Setter
@Getter
public class LoginDetails {
    private String username;
    private String password;
}