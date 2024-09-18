package com.crashcourse.kickoff.tms.tournament;

import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.*;

/*
 * Responsible for instantiating an object only
 * based on provided data, not handling logic
 */


@Data
public class Tournament {

    // Basic Identifiers
    final private Long id;
    private String name;

    // Time / Place
    private LocalDateTime start;
    private LocalDateTime end;
    private Location location;

    // Format
    final private int maxTeams;
    final private ClubFormat clubFormat; 
    final private KnockoutFormat knockoutFormat;
    private ArrayList<Float> prizePool;

    // Entry Requirements
    private int minRank;
    private int maxRank;

    // List of Clubs (Joined or Looking)
    private ArrayList<Club> joinedClubs;

    public Tournament(Long id, String name, LocalDateTime start, LocalDateTime end, Location location,
                      int maxTeams, ClubFormat clubFormat, KnockoutFormat knockoutFormat, 
                      ArrayList<Float> prizePool, int minRank, int maxRank) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.location = location;
        this.maxTeams = maxTeams;
        this.clubFormat = clubFormat;
        this.knockoutFormat = knockoutFormat;
        this.prizePool = prizePool;
        this.minRank = minRank;
        this.maxRank = maxRank;
        this.joinedClubs = new ArrayList<>();
    }
}
