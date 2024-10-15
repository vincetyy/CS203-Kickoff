package com.crashcourse.kickoff.tms.tournament.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.crashcourse.kickoff.tms.location.model.Location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Long host;

    @ElementCollection
    @CollectionTable(name = "tournament_club_ids", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "club_id")
    private List<Long> joinedClubIds = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)  
    private List<PlayerAvailability> playerAvailabilities = new ArrayList<>();  

    public String getFormat() {
        return tournamentFormat.toString();
    }

    public String getLocationName() {
        return location.getName();
    }
}
