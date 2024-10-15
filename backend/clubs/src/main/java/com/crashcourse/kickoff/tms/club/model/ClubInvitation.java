package com.crashcourse.kickoff.tms.club.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ClubInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerId;  // The player receiving the invitation

    @ManyToOne
    private Club club;  // The club sending the invitation

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;  // Using ApplicationStatus enum

    private LocalDateTime inviteSentDate;

    private LocalDateTime expirationDate;  // Optional: if you want to implement expiration logic
}
