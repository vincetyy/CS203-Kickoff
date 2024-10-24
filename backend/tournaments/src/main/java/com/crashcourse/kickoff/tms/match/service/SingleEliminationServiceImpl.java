package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import io.github.cdimascio.dotenv.Dotenv;

import com.crashcourse.kickoff.tms.match.model.*;
import com.crashcourse.kickoff.tms.match.service.*;
import com.crashcourse.kickoff.tms.match.repository.SingleEliminationBracketRepository;

import com.crashcourse.kickoff.tms.location.model.Location;
import com.crashcourse.kickoff.tms.location.repository.LocationRepository;
import com.crashcourse.kickoff.tms.location.service.LocationService;

import com.crashcourse.kickoff.tms.tournament.RestTemplateConfig;
import com.crashcourse.kickoff.tms.tournament.dto.*;
import com.crashcourse.kickoff.tms.tournament.exception.*;
import com.crashcourse.kickoff.tms.tournament.model.*;
import com.crashcourse.kickoff.tms.tournament.repository.TournamentRepository;
import com.crashcourse.kickoff.tms.security.JwtUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SingleEliminationServiceImpl implements SingleEliminationService {

    private final TournamentRepository tournamentRepository;
    private final SingleEliminationBracketRepository bracketRepository;
    private final RoundService roundService;

    @Override
    public Bracket createBracket(Long tournamentId, int numberOfClubs) {
        if (numberOfClubs == 0) {
            throw new EntityNotFoundException("No clubs found");
        }
        Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));
        

        int numberOfRounds = (int) Math.ceil(Math.log(numberOfClubs) / Math.log(2));

        SingleEliminationBracket bracket = new SingleEliminationBracket();
        List<Round> bracketRounds = new ArrayList<>();

        while (numberOfRounds > 0) {
            int size = (int) Math.pow(2, numberOfRounds);
            bracketRounds.add(roundService.createRound(size));
            numberOfRounds--;
        }
    
        bracket.setRounds(bracketRounds);
        bracketRepository.save(bracket);

        tournament.setBracket(bracket);
        tournamentRepository.save(tournament);
        return bracket;
    }
}
