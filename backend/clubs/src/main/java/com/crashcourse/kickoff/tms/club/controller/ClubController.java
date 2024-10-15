package com.crashcourse.kickoff.tms.club.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crashcourse.kickoff.tms.club.dto.*;
import com.crashcourse.kickoff.tms.security.JwtUtil;
import com.crashcourse.kickoff.tms.club.model.Club;
import com.crashcourse.kickoff.tms.club.service.ClubService.*;
import com.crashcourse.kickoff.tms.club.service.ClubServiceImpl;
import com.crashcourse.kickoff.tms.club.model.ClubProfile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

    @Autowired
    private ClubServiceImpl clubService;
    @Autowired
    private final JwtUtil jwtUtil; // final for constructor injection

    @PostMapping("/create-club")
    public ResponseEntity<?> createClub(@Valid @RequestBody ClubCreationRequest clubRequest,
     @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        try {
            token = token.substring(7);
            Long userIdFromToken = jwtUtil.extractUserId(token);
            Club createdClub = clubService.createClub(clubRequest.getClub(), userIdFromToken);
            return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Club> getAllClubs() {
        return clubService.getAllClubs();
    }

    // @GetMapping("/{clubId}")
    // public ResponseEntity<?> getClubById(@PathVariable Long clubId) {
    //     Optional<Club> club = clubService.getClubById(clubId);
    //     if (club.isPresent()) {
    //         return new ResponseEntity<>(club.get(), HttpStatus.OK);
    //     }
    //     return new ResponseEntity<String>("Club not found", HttpStatus.NOT_FOUND);
    // }

    @PutMapping("/{clubId}")
    public ResponseEntity<?> updateClub(@PathVariable Long clubId, @RequestBody Club clubDetails) {
        try {
            Club updatedClub = clubService.updateClub(clubId, clubDetails);
            return new ResponseEntity<>(updatedClub, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<?> deleteClub(@PathVariable Long clubId) {
        clubService.deleteClub(clubId);
        return new ResponseEntity<>("Club deleted successfully", HttpStatus.OK);
    }

    @PatchMapping("/{clubId}/transferCaptain")
    public ResponseEntity<?> transferCaptaincy(@PathVariable Long clubId, @RequestBody CaptainTransferRequest request) {
        try {
            Club updatedClub = clubService.transferCaptaincy(clubId, request.getCurrentCaptainId(), request.getNewCaptainId());
            return new ResponseEntity<>(updatedClub, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{clubId}/addPlayer")
    public ResponseEntity<?> addPlayerToClub(@PathVariable Long clubId, @RequestBody Long playerId) {
        try {
            Club updatedClub = clubService.addPlayerToClub(clubId, playerId);
            return new ResponseEntity<>(updatedClub, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{clubId}/removePlayer")
    public ResponseEntity<?> removePlayerFromClub(@PathVariable Long clubId, @RequestBody Long playerId) {
        try {
            Club updatedClub = clubService.removePlayerFromClub(clubId, playerId);
            return new ResponseEntity<>(updatedClub, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{clubId}/apply")
    public ResponseEntity<?> applyToClub(@PathVariable Long clubId, @RequestBody PlayerApplicationDTO applicationDTO) {
        try {
            applicationDTO.setClubId(clubId);
            clubService.applyToClub(applicationDTO);
            return new ResponseEntity<>("Application submitted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{clubId}/invite")
    public ResponseEntity<?> invitePlayerToClub(@PathVariable Long clubId, @RequestBody PlayerInviteRequest inviteRequest) {
        try {
            clubService.invitePlayerToClub(clubId, inviteRequest.getPlayerId(), inviteRequest.getCaptainId());
            return new ResponseEntity<>("Invitation sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{clubId}/players")
    public ResponseEntity<?> getPlayersFromClub(@PathVariable Long clubId) {
        try {
            List<Long> players = clubService.getPlayers(clubId);
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubProfile> getClubProfile(@PathVariable Long id) {
        Optional<Club> club = clubService.getClubById(id);
        if (club.isPresent()) {
            Club existingClub = club.get();
            ClubProfile profile = new ClubProfile(existingClub);
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<Club> getClubByPlayerId(@PathVariable Long playerId) {
        return clubService.getClubByPlayerId(playerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{clubId}/applications")
    public ResponseEntity<List<Long>> getPlayerApplications(@PathVariable Long clubId) {
        List<Long> applicants = clubService.getPlayerApplications(clubId);
        if (applicants == null) {
            throw new RuntimeException("help");
        }
        return new ResponseEntity<>(applicants, HttpStatus.OK);
    }

    @PostMapping("/{clubId}/applications/{playerId}")
    public ResponseEntity<?> processApplication(@PathVariable Long clubId, @PathVariable Long playerId, 
                                                                @RequestBody ApplicationUpdateDTO body) {
        System.out.println(body.getApplicationStatus());
        String status = body.getApplicationStatus();
        if (status.equals("ACCEPTED")) {
            System.out.printf("APPLICATION ACCEPTED\n");

            clubService.acceptApplication(clubId, playerId);
            return new ResponseEntity<>(HttpStatus.OK);

        } else if (status.equals("REJECTED")) {
            System.out.printf("APPLICATION REJECTED\n");

            clubService.rejectApplication(clubId, playerId);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}