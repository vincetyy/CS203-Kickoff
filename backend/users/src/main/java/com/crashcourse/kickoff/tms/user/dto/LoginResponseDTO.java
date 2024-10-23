package com.crashcourse.kickoff.tms.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private Long userId;
    private String jwtToken;
    private boolean isAdmin;
}