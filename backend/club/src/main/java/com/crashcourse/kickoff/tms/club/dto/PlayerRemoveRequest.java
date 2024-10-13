package com.crashcourse.kickoff.tms.club.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayerRemoveRequest {

    @NotNull(message = "Player ID is required")
    private Long playerId;

    @NotNull(message = "Captain ID is required")
    private Long captainId;

    // Constructor
    public PlayerRemoveRequest() {}

    public PlayerRemoveRequest(Long playerId, Long captainId) {
        this.playerId = playerId;
        this.captainId = captainId;
    }
}
