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
    public Match createMatch(Long roundId, Long matchNumber) {
        Match match = new Match();
        match.setMatchNumber(matchNumber);

        Round round = roundRepository.findById(roundId)
            .orElseThrow(() -> new EntityNotFoundException("Round not found with id: " + roundId));
        match.setRound(round);
        return matchRepository.save(match);
    }
    

    @Override
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Match not found with ID: " + id));
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
