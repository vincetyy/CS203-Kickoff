package com.crashcourse.kickoff.tms.tournament;

import java.util.ArrayList;
import java.time.LocalDateTime;
import lombok.*;

/*
 * Responsible for all the CRUD functions with
 * respect to tournaments in the service layer
 */


public class TournamentService {

    // @Autowired
    private TournamentRepository tournamentRepository;

    // Create
    public Tournament createTournament(String name, LocalDateTime start, LocalDateTime end, 
                                       Location location, int maxTeams, ClubFormat clubFormat, 
                                       KnockoutFormat knockoutFormat, ArrayList<Float> prizePool,
                                       int minRank, int maxRank) {

        // decide id here
        long val = 0;
        Tournament tournament = new Tournament(val, name, start, end, location, maxTeams, clubFormat, 
                                               knockoutFormat, prizePool, minRank, maxRank);
        // return tournamentRepository.save(tournament);
        return null;
    }

    // Read (All)

    // Read (ID)
    public Tournament findTournamentByID(int id) {
        return null;
    }
    // Read by other forms? filtering system

    // Update
    public Tournament updateTournament() {
        return null;
    }

    // Delete
    public void deleteTournament() {
        return;
    }

    // Exists

    // 

}
