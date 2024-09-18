package com.crashcourse.kickoff.tms.tournament;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.time.LocalDateTime;
import lombok.*;

/*
 * Responsible for all the CRUD functions with
 * respect to tournaments in the service layer
 */

@Service
public class TournamentService {

    // @Autowired
    private TournamentRepository tournamentRepository;
    private static long id = 0;

    // Create
    public Tournament createTournament(String name, LocalDateTime start, LocalDateTime end, 
                                       Location location, int maxTeams, TournamentFormat clubFormat, 
                                       KnockoutFormat knockoutFormat, ArrayList<Float> prizePool,
                                       int minRank, int maxRank) {

        Tournament tournament = new Tournament(id, name, start, end, location, maxTeams, clubFormat, 
                                               knockoutFormat, prizePool, minRank, maxRank);
        id++;
        // return tournamentRepository.save(tournament);
        return tournament;
    }

    // Read - All Tournaments
    public Tournament findAllTournaments() {
        return null;
    }

    // Read - ID
    public Tournament findTournamentByID(int id) {
        return null;
    }
    
    // Read - Location
    public Tournament findTournamentByLocation(Location location) {
        return null;
    }

    // Read - Any other things

    // Update
    public Tournament updateTournament() {
        return null;
    }

    // Delete
    public void deleteTournament(long id) {
        return;
    }

    // Exists

    // 

}
