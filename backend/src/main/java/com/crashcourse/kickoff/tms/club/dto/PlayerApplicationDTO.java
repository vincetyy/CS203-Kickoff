package com.crashcourse.kickoff.tms.club.dto;

import com.crashcourse.kickoff.tms.player.PlayerPosition;

public class PlayerApplicationDTO {

    private Long playerProfileId;
    private Long clubId;
    private PlayerPosition desiredPosition;

    // Constructors, getters, and setters

    public Long getPlayerProfileId() {
        return playerProfileId;
    }

    public void setPlayerProfileId(Long playerProfileId) {
        this.playerProfileId = playerProfileId;
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
