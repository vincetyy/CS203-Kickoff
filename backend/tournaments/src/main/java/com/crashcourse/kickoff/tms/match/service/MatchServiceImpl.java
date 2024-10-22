package com.crashcourse.kickoff.tms.match.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.repository.MatchRepository;
import com.crashcourse.kickoff.tms.match.dto.*;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;

    @Override
    public MatchResponseDTO createMatch(Long tournamentId, Long userId, Long parentId) {

        Match match = new Match();
        match.setTournamentId(tournamentId);

        /*
         * Update Parent
         */
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

        /*
         * Save matches
         */
        matchRepository.save(parentMatch);
        Match savedMatch = matchRepository.save(match);

        return mapToResponseDTO(savedMatch);
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

                match.getTournamentId(),
                match.getLeftChild().getId(),
                match.getRightChild().getId(),
                match.getParentMatch().getId(),

                match.getClub1Id(),
                match.getClub2Id(),

                match.getClub1Score(),
                match.getClub2Score(),
                match.getWinningClubId()
        );
    }

}
