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
    public String createTournament(@RequestBody Tournament TournamentDetails) {
        //tournamentRepository.save(TournamentDetails);
        return "hey team";
    }
}
