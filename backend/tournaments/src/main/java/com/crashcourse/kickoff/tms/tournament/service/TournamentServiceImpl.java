package com.crashcourse.kickoff.tms.tournament.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import io.github.cdimascio.dotenv.Dotenv;

import com.crashcourse.kickoff.tms.client.ClubServiceClient;

import com.crashcourse.kickoff.tms.club.ClubProfile;

import com.crashcourse.kickoff.tms.match.model.*;
import com.crashcourse.kickoff.tms.match.service.*;
import com.crashcourse.kickoff.tms.match.dto.MatchUpdateDTO;
import com.crashcourse.kickoff.tms.match.dto.MatchResponseDTO;
import com.crashcourse.kickoff.tms.match.repository.MatchRepository;

import com.crashcourse.kickoff.tms.location.model.Location;
import com.crashcourse.kickoff.tms.location.repository.LocationRepository;
import com.crashcourse.kickoff.tms.location.service.LocationService;

import com.crashcourse.kickoff.tms.tournament.RestTemplateConfig;
import com.crashcourse.kickoff.tms.tournament.dto.*;
import com.crashcourse.kickoff.tms.tournament.exception.*;
import com.crashcourse.kickoff.tms.tournament.model.*;
import com.crashcourse.kickoff.tms.tournament.repository.PlayerAvailabilityRepository;
import com.crashcourse.kickoff.tms.tournament.repository.TournamentRepository;

import com.crashcourse.kickoff.tms.security.JwtUtil;
import com.crashcourse.kickoff.tms.security.JwtTokenProvider;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of TournamentService.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TournamentServiceImpl implements TournamentService {
    
    private final TournamentRepository tournamentRepository;
    private final LocationRepository locationRepository;
    private final MatchRepository matchRepository;

    private final LocationService locationService;
    private final PlayerAvailabilityRepository playerAvailabilityRepository;

    private final SingleEliminationService singleEliminationService;

    private Dotenv dotenv;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RestTemplate restTemplate;

    /*
     * Microservice Communication
     */
    private final ClubServiceClient clubServiceClient;


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
    @Transactional
    public TournamentResponseDTO updateTournament(Long id, TournamentUpdateDTO dto) {
        Tournament existingTournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + id));

        existingTournament.setName(dto.getName());
        existingTournament.setStartDateTime(dto.getStartDateTime());
        existingTournament.setEndDateTime(dto.getEndDateTime());

        Location location = locationRepository.findById(dto.getLocation().getId())
            .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + dto.getLocation().getId()));
        existingTournament.setLocation(location);
        location.getTournaments().add(existingTournament);

        existingTournament.setPrizePool(dto.getPrizePool());
        existingTournament.setMinRank(dto.getMinRank());
        existingTournament.setMaxRank(dto.getMaxRank());

        Tournament updatedTournament = tournamentRepository.save(existingTournament);
        return mapToResponseDTO(updatedTournament);
    }

    public TournamentResponseDTO startTournament(Long id, String jwtToken) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + id));
        if (tournament.getJoinedClubIds() == null || tournament.getJoinedClubIds().size() == 0) {
            throw new EntityNotFoundException("No clubs found in tournament with id: " + id);
        }
        /*
         * Prevent re-creation of bracket
         */
        if (tournament.getBracket() != null) {
            throw new RuntimeException("Bracket has already been created for tournament with id: " + id);
        }

        /*
         * Adding this to handle different format bracket creation
         */
        KnockoutFormat knockoutFormat = tournament.getKnockoutFormat();
        if (KnockoutFormat.SINGLE_ELIM.equals(knockoutFormat)) {
            Bracket bracket = singleEliminationService.createBracket(id, tournament.getJoinedClubIds(), jwtToken);
            tournament.setBracket(bracket);
            Tournament savedTournament = tournamentRepository.save(tournament);
            return mapToResponseDTO(savedTournament);
        } else {
            throw new UnsupportedOperationException("Unsupported tournament format: " + knockoutFormat);
        }
    }

    public Match updateMatchInTournament(Long tournamentId, Long matchId, MatchUpdateDTO matchUpdateDTO, String jwtToken) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new EntityNotFoundException("Tournament not found with ID: " + tournamentId));

        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new EntityNotFoundException("Match not found with ID: " + matchId));

        Long club1Id = matchUpdateDTO.getClub1Id();
        Long club2Id = matchUpdateDTO.getClub2Id();

        /*
         * Validation for Club1 - microservice interaction handled in ClubServiceClient
         */
        ClubProfile clubProfile = clubServiceClient.getClubProfileById(club1Id, jwtToken);
        if (clubProfile == null) {
            throw new RuntimeException("Club 1 profile not found.");
        }

        /*
         * Validation for Club2
         */
        clubProfile = clubServiceClient.getClubProfileById(club2Id, jwtToken);
        if (clubProfile == null) {
            throw new RuntimeException("Club 2 profile not found.");
        }

        /*
         * Validation for Winning Club
         */
        Long winningClubId = matchUpdateDTO.getWinningClubId();
        if (!winningClubId.equals(matchUpdateDTO.getClub1Id()) &&
                            !winningClubId.equals(matchUpdateDTO.getClub2Id())) {
            throw new RuntimeException("Invalid winning club");
        }

        /*
         * Update Clubs
         */
        match.setClub1Id(matchUpdateDTO.getClub1Id());
        match.setClub2Id(matchUpdateDTO.getClub2Id());

        /*
         * Update Score
         */
        match.setClub1Score(matchUpdateDTO.getClub1Score());
        match.setClub2Score(matchUpdateDTO.getClub2Score());
        match.setWinningClubId(winningClubId);

        /*
         * Adding this to handle different bracket progression formats
         */
        KnockoutFormat knockoutFormat = tournament.getKnockoutFormat();
        if (KnockoutFormat.SINGLE_ELIM.equals(knockoutFormat)) {
            Match updatedMatch = singleEliminationService.updateMatch(tournament, match, matchUpdateDTO);
            return updatedMatch;
        } else {
            throw new UnsupportedOperationException("Unsupported tournament format: " + knockoutFormat);
        }
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
    private Tournament mapToEntity(TournamentCreateDTO dto, Long host) {
        Tournament tournament = new Tournament();
        tournament.setName(dto.getName());
        tournament.setStartDateTime(dto.getStartDateTime());
        tournament.setEndDateTime(dto.getEndDateTime());

        Location location = locationRepository.findById(dto.getLocation().getId())
            .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + dto.getLocation().getId()));
        tournament.setLocation(location);
        location.getTournaments().add(tournament);

        tournament.setMaxTeams(dto.getMaxTeams());
        tournament.setTournamentFormat(dto.getTournamentFormat());
        tournament.setKnockoutFormat(dto.getKnockoutFormat());
        tournament.setPrizePool(dto.getPrizePool());
        tournament.setMinRank(dto.getMinRank());
        tournament.setMaxRank(dto.getMaxRank());
        /*
         * If you want i can add a custom exception ,im leaving this for now
         */

        tournament.setHost(host);

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

        List<Long> clubIds = tournament.getJoinedClubIds().stream()
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
                clubIds,
                tournament.getHost(),
                tournament.getBracket()
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
    public TournamentResponseDTO joinTournamentAsClub(TournamentJoinDTO dto, String jwtToken) {
        Long tournamentId = dto.getTournamentId();
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + tournamentId));

        /*
        * Refer to ClubController.java
        * Now receiving a ClubProfile instead of a list of player IDs
        */
        Long clubId = dto.getClubId();
        ClubProfile clubProfile = clubServiceClient.getClubProfileById(clubId, jwtToken);
        if (clubProfile == null) {
            throw new RuntimeException("Club profile not found.");
        }
        
        /*
         * Validate to check if user is Captain of club
         */
        try {
            Long userIdFromToken = jwtUtil.extractUserId(jwtTokenProvider.getToken(jwtToken));
        } catch (Exception e) {
            System.out.println(e);
        }
        // if (clubProfile.getCaptainId() == null || clubProfile.getCaptainId() != userIdFromToken) {
        //     throw new RuntimeException("Only a club captain can join the tournament for the club.");

        List<Long> players = clubProfile.getPlayers();
        if (players == null || tournament.getTournamentFormat().getNumberOfPlayers() > players.size()) {
            throw new NotEnoughPlayersException("Club does not have enough players.");
        }

        if (tournament.getJoinedClubIds() != null && tournament.getJoinedClubIds().contains(clubId)) {
            throw new ClubAlreadyJoinedException("Club has already joined the tournament.");
        }

        if (tournament.getJoinedClubIds().size() >= tournament.getMaxTeams()) {
            throw new TournamentFullException("Tournament is already full.");
        }

        /*
         * Validation for elo range
         */
        double elo = clubProfile.getElo();
        if (tournament.getMinRank() != null && elo < tournament.getMinRank()) {
            throw new RuntimeException("Club does not meet tournament minimum elo requirement.");
        }
        if (tournament.getMaxRank() != null && elo > tournament.getMaxRank()) {
            throw new RuntimeException("Club exceeds tournament maximum elo requirement.");
        }

        tournament.getJoinedClubIds().add(clubId);
        Tournament updatedTournament = tournamentRepository.save(tournament);
        return mapToResponseDTO(updatedTournament);
    }


    public void removeClubFromTournament(Long tournamentId, Long clubId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        
        // check clubId is part of the tournament
        if (!tournament.getJoinedClubIds().contains(clubId)) {
            throw new RuntimeException("Club is not part of the tournament");
        }

        // Remove the club from the tournament
        tournament.getJoinedClubIds().remove(clubId);
        
        // Save the tournament after modification
        tournamentRepository.save(tournament);
    }

    /**
     * @param id Tournament ID
     * @return List of Club objects
     */
    @Override
    @Transactional(readOnly = true)
    public List<Long> getAllClubsInTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + id));
        return tournament.getJoinedClubIds();
    }

    // check if the username in the claim is indeed the profile id in the request variable
    public boolean isOwnerOfTournament(Long tournamentId, Long profileId) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(tournamentId);
        if (tournamentOpt.isPresent()) {
            Tournament tournament = tournamentOpt.get();
            return tournament.getHost().equals(profileId);
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

    // @Override
    // @Transactional(readOnly = true)
    // public List<TournamentResponseDTO> getTournamentsForPlayer(Long playerId, TournamentFilter filter) {
    //     List<Tournament> tournaments;

    //     String clubServiceUrl = "http://localhost:8082/clubs/" + clubId + "/players";

    //     JwtUtil help = new JwtUtil();
    //     String jwtToken = help.generateJwtToken();

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Authorization", "Bearer " + jwtToken);
    //     HttpEntity<Long> request = new HttpEntity<>(clubId, headers);
    //     System.out.println((request));

    //     ResponseEntity<List<Long>> response = restTemplate.exchange(
    //         clubServiceUrl, 
    //         HttpMethod.GET, 
    //         request, 
    //         new ParameterizedTypeReference<List<Long>>() {}
    //     );
    //     System.out.println(response);

    //     switch (filter) {
    //         case UPCOMING:
    //             tournaments = tournamentRepository.findUpcomingTournamentsForClub(clubId);
    //             break;
    //         case CURRENT:
    //             tournaments = tournamentRepository.findCurrentTournamentsForClub(clubId);
    //             break;
    //         case PAST:
    //             tournaments = tournamentRepository.findPastTournamentsForClub(clubId);
    //             break;
    //         default:
    //             throw new IllegalArgumentException("Invalid filter type");
    //     }

    //     return tournaments.stream()
    //             .map(this::mapToResponseDTO)
    //             .collect(Collectors.toList());
    // }

    @Override
    public PlayerAvailability updatePlayerAvailability(PlayerAvailabilityDTO dto) {
        
        Long clubId = dto.getClubId();
        
        if (clubId == null) {
            throw new RuntimeException("You must join a club before indicating availability.");
        }
        
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
            .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + dto.getTournamentId()));
        
        PlayerAvailability playerAvailability = playerAvailabilityRepository
            .findByTournamentIdAndPlayerId(dto.getTournamentId(), dto.getPlayerId())
            .orElse(new PlayerAvailability());

        playerAvailability.setTournament(tournament);
        playerAvailability.setPlayerId(dto.getPlayerId());
        playerAvailability.setClubId(clubId);
        playerAvailability.setAvailable(dto.isAvailable());

        try {
            playerAvailabilityRepository.save(playerAvailability);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update player availability: " + e.getMessage(), e);
        }

        return playerAvailability;
    }


    @Override
    public List<PlayerAvailabilityDTO> getPlayerAvailabilityForTournament(Long tournamentId) {
        List<PlayerAvailability> availabilities = playerAvailabilityRepository.findByTournamentId(tournamentId);
        return availabilities.stream()
            .map(availability -> new PlayerAvailabilityDTO(
                tournamentId,
                availability.getPlayerId(),
                availability.getClubId(), 
                availability.isAvailable()
            ))
            .collect(Collectors.toList());
    }

    public List<Tournament> getHostedTournaments(Long host) {
        List<Tournament> hostedTournaments = tournamentRepository.findByHost(host);
        return hostedTournaments;
    }

}
