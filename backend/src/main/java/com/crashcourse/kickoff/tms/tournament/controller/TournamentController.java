package com.crashcourse.kickoff.tms.tournament.controller;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.security.JwtUtil;
import com.crashcourse.kickoff.tms.tournament.dto.*;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFilter;
import com.crashcourse.kickoff.tms.tournament.service.TournamentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @Valid @RequestBody TournamentCreateDTO tournamentCreateDTO,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
            ) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid" + token);
        }
        token = token.substring(7);
        Long userIdFromToken = jwtUtil.extractUserId(token);
        if (id != userIdFromToken) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this tournament");
        }
        TournamentResponseDTO updatedTournament = tournamentService.updateTournament(id, tournamentCreateDTO);
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
    public ResponseEntity<TournamentResponseDTO> joinTournamentAsClub(
            @Valid @RequestBody TournamentJoinDTO tournamentJoinDTO) {
        TournamentResponseDTO joinedTournament = tournamentService.joinTournamentAsClub(tournamentJoinDTO);
        return new ResponseEntity<>(joinedTournament, HttpStatus.CREATED);
    }

    /**
     * Retrieve all Tournaments.
     *
     * @return ResponseEntity with the list of clubs for a given tournament.
     */
    @GetMapping("/{id}/clubs")
    public ResponseEntity<List<Club>> getClubsInTournament(@PathVariable Long id) {
        List<Club> clubs = tournamentService.getAllClubsInTournament(id);
        return ResponseEntity.ok(clubs);
    }

    @GetMapping("/{clubId}/tournaments")
    public ResponseEntity<List<TournamentResponseDTO>> getTournamentsForClub(
            @PathVariable Long clubId,
            @RequestParam TournamentFilter filter) {
        List<TournamentResponseDTO> tournaments = tournamentService.getTournamentsForClub(clubId, filter);
        return ResponseEntity.ok(tournaments);
    }


}
