package com.crashcourse.kickoff.tms.tournament.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crashcourse.kickoff.tms.security.JwtUtil;
import com.crashcourse.kickoff.tms.tournament.dto.PlayerAvailabilityDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentCreateDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentJoinDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentResponseDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentUpdateDTO;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFilter;
import com.crashcourse.kickoff.tms.tournament.service.TournamentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Tournaments.
 * Provides endpoints to create, retrieve, update, delete, and list tournaments.
 */
@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final JwtUtil jwtUtil; // final for constructor injection

    /**
     * Create a new Tournament.
     *
     * @param tournamentCreateDTO DTO containing tournament creation data.
     * @return ResponseEntity with the created Tournament data and HTTP status.
     */
    @PostMapping
    public ResponseEntity<TournamentResponseDTO> createTournament(
            @Valid @RequestBody TournamentCreateDTO tournamentCreateDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = token.substring(7);
        Long userIdFromToken = jwtUtil.extractUserId(token);
        TournamentResponseDTO createdTournament = tournamentService.createTournament(tournamentCreateDTO, userIdFromToken);
        return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
    }

    /**
     * Retrieve a Tournament by its ID.
     *
     * @param id ID of the tournament to retrieve.
     * @return ResponseEntity with the Tournament data and HTTP status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> getTournamentById(@PathVariable Long id) {
        TournamentResponseDTO tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }

    /**
     * Retrieve all Tournaments.
     *
     * @return ResponseEntity with the list of Tournaments and HTTP status.
     */
    @GetMapping
    public ResponseEntity<List<TournamentResponseDTO>> getAllTournaments() {
        List<TournamentResponseDTO> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    /**
     * Update an existing Tournament.
     *
     * @param id                  ID of the tournament to update.
     * @param tournamentCreateDTO DTO containing updated tournament data.
     * @return ResponseEntity with the updated Tournament data and HTTP status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTournament(
            @PathVariable Long id,
            @Valid @RequestBody TournamentUpdateDTO tournamentUpdateDTO,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
            ) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid" + token);
        }
        token = token.substring(7);
        Long userIdFromToken = jwtUtil.extractUserId(token);
        
        boolean isOwnerOfTournament = tournamentService.isOwnerOfTournament(id,userIdFromToken);

        if (!isOwnerOfTournament) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this tournament");
        }
        TournamentResponseDTO updatedTournament = tournamentService.updateTournament(id, tournamentUpdateDTO);
        return ResponseEntity.ok(updatedTournament);
    }

    /**
     * Delete a Tournament by its ID.
     *
     * @param id ID of the tournament to delete.
     * @return ResponseEntity with HTTP status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Join a Tournament.
     *
     * @param tournamentJoinDTO DTO containing tournament creation data.
     * @return ResponseEntity with the new Tournament data and HTTP status.
     */
    @PostMapping("/join")
    public ResponseEntity<?> joinTournamentAsClub(
            @Valid @RequestBody TournamentJoinDTO tournamentJoinDTO,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid" + token);
        }

        /*
            NOTE: i removed check on whether the user is the captain
            this is because i have to remove clubservice instance var to remove club dependency
            i think we can handle this in the front end, but im not sure, will note down
        */
        TournamentResponseDTO joinedTournament = tournamentService.joinTournamentAsClub(tournamentJoinDTO);

        return new ResponseEntity<>(joinedTournament, HttpStatus.CREATED);
    }

    /**
     * Retrieve all Tournaments.
     *
     * @return ResponseEntity with the list of clubs for a given tournament.
     */
    @GetMapping("/{id}/clubs")
    public ResponseEntity<List<Long>> getClubsInTournament(@PathVariable Long id) {
        List<Long> clubIds = tournamentService.getAllClubsInTournament(id);
        return ResponseEntity.ok(clubIds);
    }

    @DeleteMapping("/{tournamentId}/clubs/{clubId}")
    public ResponseEntity<Void> removeClubFromTournament(
            @PathVariable Long tournamentId,
            @PathVariable Long clubId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        token = token.substring(7); // Remove "Bearer " from token
        Long userIdFromToken = jwtUtil.extractUserId(token);

        // Ensure the user is authorized (e.g., check if they are the host)
        if (!tournamentService.isOwnerOfTournament(tournamentId, userIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Call the service method to remove the club
        tournamentService.removeClubFromTournament(tournamentId, clubId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{clubId}/tournaments")
    public ResponseEntity<List<TournamentResponseDTO>> getTournamentsForClub(
            @PathVariable Long clubId,
            @RequestParam TournamentFilter filter) {
        List<TournamentResponseDTO> tournaments = tournamentService.getTournamentsForClub(clubId, filter);
        return ResponseEntity.ok(tournaments);
    }

    @PutMapping("/availability")
    public ResponseEntity<?> updatePlayerAvailability(@RequestBody PlayerAvailabilityDTO dto) {

        Long tournamentId = dto.getTournamentId();
        Long playerId = dto.getPlayerId();
        Long clubId = dto.getClubId(); 
        boolean available = dto.isAvailable();
        PlayerAvailabilityDTO playerAvailabilityDTO = new PlayerAvailabilityDTO(tournamentId, playerId, clubId, available);
        tournamentService.updatePlayerAvailability(playerAvailabilityDTO);
        return ResponseEntity.ok(tournamentService.updatePlayerAvailability(playerAvailabilityDTO));
    }

    @GetMapping("/{tournamentId}/availability")
    public ResponseEntity<List<PlayerAvailabilityDTO>> getPlayerAvailability(@PathVariable Long tournamentId) {
        List<PlayerAvailabilityDTO> availabilities = tournamentService.getPlayerAvailabilityForTournament(tournamentId);
        return ResponseEntity.ok(availabilities);
    }

}
