package com.crashcourse.kickoff.tms.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    // Inject TournamentService using @Autowired
    @Autowired
    private TournamentService tournamentService;

    // The controller method for creating a Tournament with default values
    @PostMapping("/create")
    public String createTournament() {

        // Use hardcoded/default values to create a Tournament
        String name = "Champions League";
        LocalDateTime start = LocalDateTime.of(2024, 9, 18, 10, 0); // Example start date
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 18, 0);   // Example end date
        Location location = new Location();
        int maxTeams = 16;
        ClubFormat clubFormat = ClubFormat.FIVE_SIDE; // Example club format
        KnockoutFormat knockoutFormat = KnockoutFormat.SINGLE_ELIM; // Example knockout format
        ArrayList<Float> prizePool = new ArrayList<>(); // Example prize pool
        prizePool.add(100000.0f);
        prizePool.add(50000.0f);
        int minRank = 1; // Example min rank
        int maxRank = 100; // Example max rank

        Tournament tourney = tournamentService.createTournament(
            name, start, end, location, maxTeams, clubFormat, knockoutFormat, prizePool, minRank, maxRank);
        // Call the service method to create the Tournament
        return tourney.toString();
    }
}
