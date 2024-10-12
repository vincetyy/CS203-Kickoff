package com.crashcourse.kickoff.tms.club.model;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.player.PlayerProfile;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ClubInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PlayerProfile playerProfile;  // The player receiving the invitation

    @ManyToOne
    private Club club;  // The club sending the invitation

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;  // Using ApplicationStatus enum

    private LocalDateTime inviteSentDate;

    private LocalDateTime expirationDate;  // Optional: if you want to implement expiration logic
}
