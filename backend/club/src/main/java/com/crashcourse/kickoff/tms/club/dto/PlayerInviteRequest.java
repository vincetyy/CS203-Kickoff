package com.crashcourse.kickoff.tms.club.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayerInviteRequest {

    @NotNull(message = "Player ID is required")
    private Long playerId;

    @NotNull(message = "Captain ID is required")
    private Long captainId;

    // Constructor
    public PlayerInviteRequest() {}

    public PlayerInviteRequest(Long playerId, Long captainId) {
        this.playerId = playerId;
        this.captainId = captainId;
    }
}
