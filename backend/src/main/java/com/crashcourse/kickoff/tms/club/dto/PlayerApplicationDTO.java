package com.crashcourse.kickoff.tms.club.dto;

import com.crashcourse.kickoff.tms.user.model.PlayerPosition;

public class PlayerApplicationDTO {

    private Long playerId;
    private Long clubId;
    private PlayerPosition desiredPosition;

    // Constructors, getters, and setters

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public PlayerPosition getDesiredPosition() {
        return desiredPosition;
    }

    public void setDesiredPosition(PlayerPosition desiredPosition) {
        this.desiredPosition = desiredPosition;
    }
}
