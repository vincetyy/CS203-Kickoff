package com.crashcourse.kickoff.tms.club.model;

import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.user.model.PlayerPosition;
import jakarta.persistence.*;

@Entity
public class PlayerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User player;  // Reference User

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
    public PlayerApplication(User player, Club club, PlayerPosition desiredPosition, ApplicationStatus status) {
        this.player = player;
        this.club = club;
        this.desiredPosition = desiredPosition;
        this.status = status;
    }

    // Getters and Setters
    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
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
