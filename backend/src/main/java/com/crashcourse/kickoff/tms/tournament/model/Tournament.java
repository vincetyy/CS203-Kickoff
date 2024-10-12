package com.crashcourse.kickoff.tms.tournament.model;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.host.HostProfile;
import com.crashcourse.kickoff.tms.location.model.Location;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.tournament.model.PlayerAvailability;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)  
    private List<PlayerAvailability> playerAvailabilities = new ArrayList<>();  

    public String getFormat() {
        return tournamentFormat.toString();
    }

    public String getLocationName() {
        return location.getName();
    }
}
