package com.crashcourse.kickoff.tms.club.dto;

import lombok.Data;

@Data
public class ClubApplicationRequest {
    private Long playerId;
    private String desiredPosition;
}