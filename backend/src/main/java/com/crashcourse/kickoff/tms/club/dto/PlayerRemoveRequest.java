package com.crashcourse.kickoff.tms.club.dto;

import jakarta.validation.constraints.NotNull;

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

    // Getters and Setters
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getCaptainId() {
        return captainId;
    }

    public void setCaptainId(Long captainId) {
        this.captainId = captainId;
    }
}
