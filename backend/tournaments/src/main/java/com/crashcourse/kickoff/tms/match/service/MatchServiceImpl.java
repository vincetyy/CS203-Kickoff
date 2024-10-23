package com.crashcourse.kickoff.tms.match.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.repository.MatchRepository;
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
    private TournamentRepository tournamentRepository;

    @Override
    public MatchResponseDTO createMatch(MatchCreateDTO matchCreateDTO) {

        Match match = new Match();
        Tournament tournament = tournamentRepository.findById(matchCreateDTO.getTournamentId())
            .orElseThrow(() -> new EntityNotFoundException("Parent match not found with id: " + matchCreateDTO.getTournamentId()));
        match.setTournament(tournament);

        /*
         * Save matches
         */
        Match savedMatch = matchRepository.save(match);

        return mapToResponseDTO(savedMatch);
    }

    @Override
    public List<List<Match>> createBracket(Long tournamentId, Long numberOfClubs){
        if (numberOfClubs == 0) {
            throw new EntityNotFoundException("No clubs found");
        }

        Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));

        /*
         * Calculate Number of Rounds
         */
        int numberOfRounds = (int) Math.ceil(Math.log(numberOfClubs) / Math.log(2));

        /*
         * Create brackets
         */
        List<List<Match>> tournamentMatches = new ArrayList<>();
        int roundNumber = 1;
        while (numberOfRounds - roundNumber >= 0) {

            List<Match> roundMatches = new ArrayList<>();

            int size = (int)Math.pow(2, numberOfRounds - roundNumber);
            for (int i = 0; i < size; i++) {
                Match match = new Match();
                match.setTournament(tournament);
                matchRepository.save(match);
                
                roundMatches.add(i, match);
            }
            tournamentMatches.add(roundMatches);
            roundNumber++;
        }
        return tournamentMatches;
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

                match.getTournament().getId(),
                match.getClub1Id(),
                match.getClub2Id(),

                match.getClub1Score(),
                match.getClub2Score(),
                match.getWinningClubId()
        );
    }

}
