package com.crashcourse.kickoff.tms.club.model;

import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.user.model.PlayerPosition;
import com.crashcourse.kickoff.tms.user.model.PlayerProfile;

import jakarta.persistence.*;

@Entity
public class PlayerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_profile_id", nullable = false)
    private PlayerProfile playerProfile;  // Reference User

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Enumerated(EnumType.STRING)
    private PlayerPosition desiredPosition;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    // Default constructor
    public PlayerApplication() {
    }

    // Parameterized constructor
    public PlayerApplication(PlayerProfile playerProfile, Club club, PlayerPosition desiredPosition, ApplicationStatus status) {
        this.playerProfile = playerProfile;
        this.club = club;
        this.desiredPosition = desiredPosition;
        this.status = status;
    }

    // Getters and Setters
    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public void setPlayerProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public PlayerPosition getDesiredPosition() {
        return desiredPosition;
    }

    public void setDesiredPosition(PlayerPosition desiredPosition) {
        this.desiredPosition = desiredPosition;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
