package com.crashcourse.kickoff.tms.tournament.service;

import com.crashcourse.kickoff.tms.club.*;
import com.crashcourse.kickoff.tms.club.repository.ClubRepository;
import com.crashcourse.kickoff.tms.location.service.LocationService;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.location.model.*;

import com.crashcourse.kickoff.tms.tournament.dto.*;
import com.crashcourse.kickoff.tms.tournament.exception.*;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFilter;
import com.crashcourse.kickoff.tms.tournament.repository.TournamentRepository;
import com.crashcourse.kickoff.tms.tournament.service.TournamentService;
import com.crashcourse.kickoff.tms.host.*;
import com.crashcourse.kickoff.tms.host.HostProfileServiceImpl.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of TournamentService.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TournamentServiceImpl implements TournamentService {
    private final HostProfileRepository hostProfileRepository;
    private final TournamentRepository tournamentRepository;
    private final LocationService locationService;
    private final HostProfileService hostProfileService;
    private final ClubRepository clubRepository;

    private final ClubService clubService;

    @Override
    public TournamentResponseDTO createTournament(TournamentCreateDTO dto, Long userIdFromToken) {
        Tournament tournament = mapToEntity(dto, userIdFromToken);
        Tournament savedTournament = tournamentRepository.save(tournament);
        return mapToResponseDTO(savedTournament);
    }

    @Override
    @Transactional(readOnly = true)
    public TournamentResponseDTO getTournamentById(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + id));
        return mapToResponseDTO(tournament);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TournamentResponseDTO> getAllTournaments() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        return tournaments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TournamentResponseDTO updateTournament(Long id, TournamentCreateDTO dto) {
        Tournament existingTournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + id));

        existingTournament.setName(dto.getName());
        existingTournament.setStartDateTime(dto.getStartDateTime());
        existingTournament.setEndDateTime(dto.getEndDateTime());
        existingTournament.setLocation(locationService.getLocationById(dto.getLocationId()));
        existingTournament.setMaxTeams(dto.getMaxTeams());
        existingTournament.setTournamentFormat(dto.getTournamentFormat());
        existingTournament.setKnockoutFormat(dto.getKnockoutFormat());
        existingTournament.setPrizePool(dto.getPrizePool());
        existingTournament.setMinRank(dto.getMinRank());
        existingTournament.setMaxRank(dto.getMaxRank());

        Tournament updatedTournament = tournamentRepository.save(existingTournament);
        return mapToResponseDTO(updatedTournament);
    }

    @Override
    public void deleteTournament(Long id) {
        if (!tournamentRepository.existsById(id)) {
            throw new EntityNotFoundException("Tournament not found with id: " + id);
        }
        tournamentRepository.deleteById(id);
    }

    /**
     * Maps TournamentCreateDTO to Tournament entity.
     *
     * @param dto TournamentCreateDTO
     * @return Tournament entity
     */
    private Tournament mapToEntity(TournamentCreateDTO dto, Long userIdFromToken) {
        Tournament tournament = new Tournament();
        tournament.setName(dto.getName());
        tournament.setStartDateTime(dto.getStartDateTime());
        tournament.setEndDateTime(dto.getEndDateTime());
        tournament.setLocation(locationService.getLocationById(dto.getLocationId()));
        tournament.setMaxTeams(dto.getMaxTeams());
        tournament.setTournamentFormat(dto.getTournamentFormat());
        tournament.setKnockoutFormat(dto.getKnockoutFormat());
        tournament.setPrizePool(dto.getPrizePool());
        tournament.setMinRank(dto.getMinRank());
        tournament.setMaxRank(dto.getMaxRank());
        /*
         * If you want i can add a custom exception ,im leaving this for now
         */
        HostProfile hostProfile = hostProfileRepository.findById(userIdFromToken)
                            .orElseThrow(() -> new RuntimeException("HostProfile not found"));
        tournament.setHost(hostProfile);

        return tournament;
    }

    /**
     * Maps Tournament entity to TournamentResponseDTO.
     *
     * @param tournament Tournament entity
     * @return TournamentResponseDTO
     */
    private TournamentResponseDTO mapToResponseDTO(Tournament tournament) {
        TournamentResponseDTO.LocationDTO locationDTO = new TournamentResponseDTO.LocationDTO(
                tournament.getLocation().getId(),
                tournament.getLocation().getName()
        );

        List<TournamentResponseDTO.ClubDTO> clubDTOs = tournament.getJoinedClubs().stream()
                .map(club -> new TournamentResponseDTO.ClubDTO(club.getId(), club.getName()))
                .collect(Collectors.toList());

        return new TournamentResponseDTO(
                tournament.getId(),
                tournament.getName(),
                tournament.isOver(),
                tournament.getStartDateTime(),
                tournament.getEndDateTime(),
                locationDTO,
                tournament.getMaxTeams(),
                tournament.getTournamentFormat().toString(),
                tournament.getKnockoutFormat() != null ? tournament.getKnockoutFormat().toString() : null,
                tournament.getPrizePool(),
                tournament.getMinRank(),
                tournament.getMaxRank(),
                clubDTOs,
                tournament.getHost()
        );
    }

    /**
     * Allows a club to join tournament
     *
     * @param TournamentJoinDTO dto
     * @return TournamentResponseDTO
     */
    @Transactional
    @Override
    public TournamentResponseDTO joinTournamentAsClub(TournamentJoinDTO dto) {
        Long tournamentId = dto.getTournamentId();
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));

        Long clubId = dto.getClubId();
        Club club = clubService.getClubById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));
        

        if (tournament.getJoinedClubs() != null && tournament.getJoinedClubs().contains(club)) {
            throw new ClubAlreadyJoinedException("Club has already joined the tournament.");
        }
        if (tournament.getJoinedClubs().size() >= tournament.getMaxTeams()) {
            throw new TournamentFullException("Tournament is already full.");
        }

        tournament.getJoinedClubs().add(club);

        Tournament updatedTournament = tournamentRepository.save(tournament);
        return mapToResponseDTO(updatedTournament);
    } 

    public void removeClubFromTournament(Long tournamentId, Long clubId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found"));

        // Ensure the club is part of the tournament
        if (!tournament.getJoinedClubs().contains(club)) {
            throw new RuntimeException("Club is not part of the tournament");
        }

        // Remove the club from the tournament
        tournament.getJoinedClubs().remove(club);
        
        // Save the tournament after modification
        tournamentRepository.save(tournament);
    }

    /**
     * @param id Tournament ID
     * @return List of Club objects
     */
    @Override
    @Transactional(readOnly = true)
    public List<Club> getAllClubsInTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + id));
        return tournament.getJoinedClubs();
    }

    // check if the username in the claim is indeed the profile id in the request variable
    public boolean isOwnerOfTournament(Long tournamentId, Long profileId) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);
        if (tournamentOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            System.out.println(profileId);
            System.out.println(tournament.getHost().getId());
            return tournament.getHost().getId().equals(profileId);
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TournamentResponseDTO> getTournamentsForClub(Long clubId, TournamentFilter filter) {
        List<Tournament> tournaments;

        switch (filter) {
            case UPCOMING:
                tournaments = tournamentRepository.findUpcomingTournamentsForClub(clubId);
                break;
            case CURRENT:
                tournaments = tournamentRepository.findCurrentTournamentsForClub(clubId);
                break;
            case PAST:
                tournaments = tournamentRepository.findPastTournamentsForClub(clubId);
                break;
            default:
                throw new IllegalArgumentException("Invalid filter type");
        }

        return tournaments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

}
