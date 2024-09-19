package com.crashcourse.kickoff.tms.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/tournament")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/create")
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournamentDetails) {
        Tournament createdTournament = tournamentService.createTournament(tournamentDetails);
        return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
    }
}

