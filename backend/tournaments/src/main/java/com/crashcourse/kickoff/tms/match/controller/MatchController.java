package com.crashcourse.kickoff.tms.match.controller;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import com.crashcourse.kickoff.tms.match.model.Match;
import com.crashcourse.kickoff.tms.match.model.Round;
import com.crashcourse.kickoff.tms.match.dto.*;
import com.crashcourse.kickoff.tms.match.service.MatchService;
import com.crashcourse.kickoff.tms.security.JwtUtil;

/**
 * REST Controller for managing Matches.
 * Provides endpoints to create, retrieve, update, delete, and list matches.
 */
@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    @Autowired
    private final MatchService matchService;

    @PostMapping("/{tournamentId}/createbracket")
    public ResponseEntity<?> createBracket(@PathVariable Long tournamentId, @RequestBody Long numberOfClubs) {
        try {
            List<Round> bracket = matchService.createBracket(tournamentId, numberOfClubs);
            return new ResponseEntity<>(bracket, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMatchById(@PathVariable Long id) {
        try {
            Match matchResponseDTO = matchService.getMatchById(id);
            return new ResponseEntity<>(matchResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMatch(@PathVariable Long id, @RequestBody MatchUpdateDTO matchUpdateDTO) {
        try {
            MatchResponseDTO matchResponseDTO = matchService.updateMatch(id, matchUpdateDTO);
            return new ResponseEntity<>(matchResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
