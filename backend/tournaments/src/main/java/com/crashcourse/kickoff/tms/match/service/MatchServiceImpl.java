package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.model.Round;

import com.crashcourse.kickoff.tms.match.repository.MatchRepository;
import com.crashcourse.kickoff.tms.match.repository.RoundRepository;
import com.crashcourse.kickoff.tms.match.dto.*;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.crashcourse.kickoff.tms.tournament.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    /*
     * Matches can now only be created through
     * create bracket
     */
    @Override
    public Match createMatch(Long roundId) {
        Match match = new Match();
        Round round = roundRepository.findById(roundId)
            .orElseThrow(() -> new EntityNotFoundException("Round not found with id: " + roundId));
        match.setRound(round);
        System.out.println("SFDSKFSFSD\n\n\n\n\n");
        return matchRepository.save(match);
    }

    public Round createRound(int numberOfMatches) {
        System.out.println("YES\n\n\n\n\n");
        Round round = new Round();
        // Save the round first so it has a valid ID
        round = roundRepository.save(round);
    
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < numberOfMatches; i++) {
            matches.add(createMatch(round.getId()));
        }
    
        round.setMatches(matches);
        return roundRepository.save(round);
    }
    
    @Override
    public List<Round> createBracket(Long tournamentId, Long numberOfClubs) {
        if (numberOfClubs == 0) {
            throw new EntityNotFoundException("No clubs found");
        }
    
        Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));
    
        // Calculate number of rounds
        int numberOfRounds = (int) Math.ceil(Math.log(numberOfClubs) / Math.log(2));
    
        List<Round> tournamentRounds = new ArrayList<>();
        int roundNumber = 1;
        
        while (roundNumber <= numberOfRounds) { // Adjust the condition
            System.out.println("HELLO\n\n\n\n\n");
            int size = (int) Math.pow(2, numberOfRounds - roundNumber);
            tournamentRounds.add(createRound(size));
            roundNumber++;  // Increment to avoid infinite loop
        }
    
        System.out.println("BYE\n\n\n\n\n");
        tournament.setRounds(tournamentRounds);
        tournamentRepository.save(tournament);
        return tournamentRounds;
    }
    

    @Override
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Match not found with ID: " + id));
    }

    @Override
    public MatchResponseDTO updateMatch(Long id, MatchUpdateDTO matchUpdateDTO) {
        Match foundMatch = matchRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Match not found with ID: " + id));

        /*
         * Update Score
         */
        foundMatch.setClub1Score(matchUpdateDTO.getClub1Score());
        foundMatch.setClub2Score(matchUpdateDTO.getClub2Score());
        foundMatch.setWinningClubId(matchUpdateDTO.getWinningClubId());
        
        /*
         * Save to repository
         */
        matchRepository.save(foundMatch);

        return mapToResponseDTO(foundMatch);
    }

    /**
     * Maps Tournament entity to TournamentResponseDTO.
     *
     * @param tournament Tournament entity
     * @return TournamentResponseDTO
     */
    private MatchResponseDTO mapToResponseDTO(Match match) {
        return new MatchResponseDTO(
                match.getId(),
                match.isOver(),

                match.getRound().getTournament().getId(),
                match.getClub1Id(),
                match.getClub2Id(),

                match.getClub1Score(),
                match.getClub2Score(),
                match.getWinningClubId()
        );
    }

}
