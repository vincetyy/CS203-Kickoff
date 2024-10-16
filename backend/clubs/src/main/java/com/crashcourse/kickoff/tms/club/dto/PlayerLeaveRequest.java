package com.crashcourse.kickoff.tms.club.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Generates a no-args constructor
public class PlayerLeaveRequest {

    @NotNull(message = "Player ID is required")
    private Long playerId;
}
