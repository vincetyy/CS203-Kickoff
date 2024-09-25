package com.crashcourse.kickoff.tms.tournament.model;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.location.model.*;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

/*
 * Responsible for instantiating an object only
 * based on provided data, not handling logic
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean isOver;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Location location;

    private int maxTeams = 0;
    private TournamentFormat tournamentFormat;
    private KnockoutFormat knockoutFormat;
    private List<Float> prizePool;

    private int minRank;
    private int maxRank;

    @ManyToMany
    @JoinTable(
        name = "tournament_club",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private List<Club> joinedClubs = new ArrayList<>();
}
