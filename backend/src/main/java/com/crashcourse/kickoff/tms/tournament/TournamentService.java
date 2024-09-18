package com.crashcourse.kickoff.tms.tournament;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.time.LocalDateTime;
import lombok.*;

/*
 * Responsible for all the CRUD functions with
 * respect to tournaments in the service layer
 */

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    public Tournament createTournament(Tournament tournamentDetails) {
        return tournamentRepository.save(tournamentDetails);
    }

}
