package com.crashcourse.kickoff.tms.tournament.controller;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.crashcourse.kickoff.tms.security.JwtUtil;

import com.crashcourse.kickoff.tms.tournament.dto.PlayerAvailabilityDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentCreateDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentJoinDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentResponseDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentUpdateDTO;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFilter;
import com.crashcourse.kickoff.tms.tournament.service.TournamentService;

import com.crashcourse.kickoff.tms.match.model.Bracket;
import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.service.MatchService;
import com.crashcourse.kickoff.tms.match.dto.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;

/**
 * REST Controller for managing Tournaments.
 * Provides endpoints to create, retrieve, update, delete, and list tournaments.
 */
@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final MatchService matchService;
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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping("/{id}/start")
    public ResponseEntity<?> startTournament(@PathVariable Long id,
                    @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid." + token);
        }
        token = token.substring(7);
        Long userIdFromToken = jwtUtil.extractUserId(token);

        boolean isOwnerOfTournament = tournamentService.isOwnerOfTournament(id,userIdFromToken);

        if (!isOwnerOfTournament) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to start this tournament.");
        }
        return ResponseEntity.ok(tournamentService.startTournament(id));
    }

    @PutMapping("{tournamentId}/{matchId}")
    public ResponseEntity<?> updateMatchInTournament(@PathVariable Long tournamentId, @PathVariable Long matchId, 
        @RequestBody MatchUpdateDTO matchUpdateDTO, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
        try {
            Match match = tournamentService.updateMatchInTournament(tournamentId, matchId, matchUpdateDTO, token);
            return new ResponseEntity<>(match, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid" + token);
        }

        TournamentResponseDTO joinedTournament = null;
        try {
            joinedTournament = tournamentService.joinTournamentAsClub(tournamentJoinDTO, token);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e);
        }

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
        
        // if (token == null || !token.startsWith("Bearer ")) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        // }

        // token = token.substring(7); // Remove "Bearer " from token
        // Long userIdFromToken = jwtUtil.extractUserId(token);

        // // Ensure the user is authorized (e.g., check if they are the host)
        // if (!tournamentService.isOwnerOfTournament(tournamentId, userIdFromToken)) {
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        // }

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

    // @GetMapping("/player/{playerId}")
    // public ResponseEntity<List<TournamentResponseDTO>> getTournamentsForPlayer(
    //         @PathVariable Long playerId,
    //         @RequestParam TournamentFilter filter) {
    //     List<TournamentResponseDTO> tournaments = tournamentService.getTournamentsForPlayer(playerId, filter);
    //     return ResponseEntity.ok(tournaments);
    // }

    @PutMapping("/availability")
    public ResponseEntity<?> updatePlayerAvailability(@RequestBody PlayerAvailabilityDTO dto) {
        

        Long tournamentId = dto.getTournamentId();
        Long playerId = dto.getPlayerId();
        Long clubId = dto.getClubId(); 
        System.out.println(tournamentId);
        System.out.println(playerId);
        System.out.println(clubId);
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

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<Tournament>> getHostedTournaments(@PathVariable Long hostId) {
        List<Tournament> hostedTournaments = tournamentService.getHostedTournaments(hostId);
        return ResponseEntity.ok(hostedTournaments);
    }
}
