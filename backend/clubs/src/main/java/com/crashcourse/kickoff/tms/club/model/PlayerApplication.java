package com.crashcourse.kickoff.tms.club.model;

import com.crashcourse.kickoff.tms.player.PlayerPosition;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class PlayerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerId;  // Reference User

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
    public PlayerApplication(Long playerId, Club club, PlayerPosition desiredPosition, ApplicationStatus status) {
        this.playerId = playerId;
        this.club = club;
        this.desiredPosition = desiredPosition;
        this.status = status;
    }
}
