package com.crashcourse.kickoff.tms.user;

import lombok.Getter;
import lombok.Setter;

// Data Transfer Object to facilitate setting user roles on server side (allows for HTTP post requests with only username and password)
@Setter
@Getter
public class UserDTO {
    private String username;
    private String password;
}

