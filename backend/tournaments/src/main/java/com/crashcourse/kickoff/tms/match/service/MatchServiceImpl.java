package com.crashcourse.kickoff.tms.match.service;

import java.util.Optional;

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
         * Update Parent
         */
        Long parentId = matchCreateDTO.getParentId();
        if (parentId > 0) {
            Match parentMatch = matchRepository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent match not found with id: " + parentId));
            match.setParentMatch(parentMatch);

            if (parentMatch.getLeftChild() == null) {
                parentMatch.setLeftChild(match);
            } else if (parentMatch.getRightChild() == null) {
                parentMatch.setRightChild(match);
            } else {
                throw new RuntimeException("Parent match already has two children matches.");
            }
            matchRepository.save(parentMatch);

        }

        /*
         * Save matches
         */
        Match savedMatch = matchRepository.save(match);

        return mapToResponseDTO(savedMatch);
    }

    @Override
    public MatchResponseDTO getMatchById(Long id) {
        Match foundMatch = matchRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Match not found with ID: " + id));
        return mapToResponseDTO(foundMatch);
    }

    @Override
    public MatchResponseDTO updateMatch(Long id, MatchUpdateDTO matchUpdateDTO) {
        Match foundMatch = matchRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Match not found with ID: " + id));

        foundMatch.setOver(matchUpdateDTO.isOver());
        foundMatch.setClub1Id(matchUpdateDTO.getClub1Id());
        foundMatch.setClub2Id(matchUpdateDTO.getClub2Id());
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
        Long leftChildId = 0L;
        if (match.getLeftChild() != null) {
            leftChildId = match.getLeftChild().getId();
        }
        Long rightChildId = 0L;
        if (match.getRightChild() != null) {
            rightChildId = match.getRightChild().getId();
        }
        Long parentId = 0L;
        if (match.getParentMatch() != null) {
            parentId = match.getParentMatch().getId();
        }

        return new MatchResponseDTO(
                match.getId(),
                match.isOver(),

                match.getTournament().getId(),
                leftChildId,
                rightChildId,
                parentId,

                match.getClub1Id(),
                match.getClub2Id(),

                match.getClub1Score(),
                match.getClub2Score(),
                match.getWinningClubId()
        );
    }

}
