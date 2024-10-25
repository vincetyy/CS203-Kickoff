package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.http.*;

import com.crashcourse.kickoff.tms.match.model.*;
import com.crashcourse.kickoff.tms.match.service.*;
import com.crashcourse.kickoff.tms.match.dto.MatchUpdateDTO;
import com.crashcourse.kickoff.tms.match.repository.SingleEliminationBracketRepository;
import com.crashcourse.kickoff.tms.match.repository.MatchRepository;
import com.crashcourse.kickoff.tms.match.repository.RoundRepository;


import com.crashcourse.kickoff.tms.tournament.model.*;
import com.crashcourse.kickoff.tms.tournament.repository.TournamentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SingleEliminationServiceImpl implements SingleEliminationService {

    private final TournamentRepository tournamentRepository;
    private final SingleEliminationBracketRepository bracketRepository;
    private final RoundRepository roundRepository;
    private final MatchRepository matchRepository;
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
            bracketRounds.add(roundService.createRound(size, 0L + numberOfRounds));
            numberOfRounds--;
        }
    
        bracket.setRounds(bracketRounds);
        bracketRepository.save(bracket);

        bracket.setTournament(tournament);
        tournament.setBracket(bracket);
        return bracket;
    }

    @Override
    public Match updateMatch(Tournament tournament, Match match, MatchUpdateDTO matchUpdateDTO) {
        /*
         * Validation
         */
        if (tournament == null) {
            throw new EntityNotFoundException("No tournament found.");
        }
        if (match == null) {
            throw new EntityNotFoundException("No match found.");
        }

        if (!(tournament.getBracket() instanceof SingleEliminationBracket seBracket)) {
            throw new RuntimeException("Wrong bracket format.");
        }

        Long matchNumber = match.getMatchNumber();
        Long winningClubId = matchUpdateDTO.getWinningClubId();
        Long club1Id = matchUpdateDTO.getClub1Id();
        Long club2Id = matchUpdateDTO.getClub2Id();
        if (winningClubId != club1Id && winningClubId != club2Id) {
            throw new RuntimeException("Invalid winning club");
        } 

        /*
         * Update Score
         */
        match.setClub1Score(matchUpdateDTO.getClub1Score());
        match.setClub2Score(matchUpdateDTO.getClub2Score());
        match.setWinningClubId(winningClubId);

        /*
         * If over, send winner to next round
         */
        if (matchUpdateDTO.isOver()) {

            match.setOver(true);
            
            /*
             * Note that roundNumber counts downwards 
             * so we know if its the last round
             */
            if (match.getRound().getRoundNumber() == 1) {
                tournament.setWinningClubId(matchUpdateDTO.getWinningClubId());
            } else {
                List<Round> rounds = seBracket.getRounds();
                /*
                 * -1 for next round, -1 since we use 1 index
                 */
                Long roundNumber = match.getRound().getRoundNumber();
                Round nextRound = roundRepository.findRoundByRoundNumber(roundNumber - 1);
                List<Match> matches = nextRound.getMatches();

                /*
                 * -1 to account for 1 indexing again
                 */
                Match nextMatch = matches.get((int)Math.ceil(matchNumber/2.0) - 1);
                /*
                 * Note that we start from index 1
                 */
                if (matchNumber % 2 == 1) {
                    nextMatch.setClub1Id(winningClubId);
                } else {
                    nextMatch.setClub2Id(winningClubId);
                }
                matchRepository.save(nextMatch);
            }
        }
        
        return matchRepository.save(match);
    }

}
