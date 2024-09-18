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
    private Long id;
    private String name;

    // Time / Place
    private LocalDateTime start;
    private LocalDateTime end;
    private Location location;

    // Format
    private int maxTeams;
    private ClubFormat clubFormat; 
    private KnockoutFormat knockoutFormat;
    private ArrayList<Float> prizePool;

    // Entry Requirements
    private int minRank;
    private int maxRank;

    // List of Clubs (Joined or Looking)
    private ArrayList<Club> joinedClubs;

}
