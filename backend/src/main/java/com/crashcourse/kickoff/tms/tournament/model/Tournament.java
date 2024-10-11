package com.crashcourse.kickoff.tms.tournament.model;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.host.HostProfile;
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
    @JoinColumn(name = "location_id")
    private Location location;

    private Integer maxTeams = 0;
    private TournamentFormat tournamentFormat;
    private KnockoutFormat knockoutFormat;
    private List<Float> prizePool;

    private Integer minRank;
    private Integer maxRank;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private HostProfile host;

    @ManyToMany
    @JoinTable(
        name = "tournament_club",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private List<Club> joinedClubs = new ArrayList<>();

    public String getFormat() {
        return tournamentFormat.toString();
    }
    
    public String getLocationName() {
        return location.getName();
    }
    
}
