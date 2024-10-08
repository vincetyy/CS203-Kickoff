package com.crashcourse.kickoff.tms.club;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.club.exception.*;
import com.crashcourse.kickoff.tms.user.dto.PlayerInviteRequest;
import com.crashcourse.kickoff.tms.user.model.*;
import com.crashcourse.kickoff.tms.club.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    @Autowired
    private ClubServiceImpl clubService;

    @PostMapping
    public ResponseEntity<?> createClub(@Valid @RequestBody ClubCreationRequest clubRequest) {
        try {
            Club createdClub = clubService.createClub(clubRequest.getClub(), clubRequest.getCreatorId());
            return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Club> getAllClubs() {
        return clubService.getAllClubs();
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<?> getClubById(@PathVariable Long clubId) {
        Optional<Club> club = clubService.getClubById(clubId);
        if (club.isPresent()) {
            return new ResponseEntity<>(club.get(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Club not found", HttpStatus.NOT_FOUND);
    }

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
            List<PlayerProfile> players = clubService.getPlayers(clubId);
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

    // to link to free agent applying for club, in club info page
    @PostMapping("/{id}/apply")
    public ResponseEntity<?> applyToClub(@PathVariable Long id, @RequestBody ClubApplicationRequest request) {
        try {
            clubService.applyToClub(id, request.getPlayerId());
            return ResponseEntity.ok().body("Application sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}