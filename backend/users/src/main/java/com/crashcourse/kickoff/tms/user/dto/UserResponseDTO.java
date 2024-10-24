package com.crashcourse.kickoff.tms.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
}