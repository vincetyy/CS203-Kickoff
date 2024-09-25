package com.crashcourse.kickoff.tms.user.dto;
import com.crashcourse.kickoff.tms.user.model.PlayerPosition;

public class PlayerPositionDTO {
    private PlayerPosition preferredPosition;

    // Getter and Setter
    public PlayerPosition getPreferredPosition() {
        return preferredPosition;
    }

    public void setPreferredPosition(PlayerPosition preferredPosition) {
        this.preferredPosition = preferredPosition;
    }
}