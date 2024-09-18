package com.crashcourse.kickoff.tms.tournament;

import com.crashcourse.kickoff.tms.club.Club;

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

    @OneToMany
    private List<Club> joinedClubs = new ArrayList<>();
}
