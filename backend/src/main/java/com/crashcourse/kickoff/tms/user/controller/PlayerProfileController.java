package com.crashcourse.kickoff.tms.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.club.ClubService;
import com.crashcourse.kickoff.tms.user.dto.AcceptInvitationRequest;
import com.crashcourse.kickoff.tms.user.dto.PlayerPositionDTO;
import com.crashcourse.kickoff.tms.user.model.PlayerPosition;
import com.crashcourse.kickoff.tms.user.model.PlayerProfile;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.service.PlayerProfileService;
import com.crashcourse.kickoff.tms.user.service.UserService;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/players")
public class PlayerProfileController {

    private final PlayerProfileService playerProfileService;
    private final ClubService clubService;  // final for constructor injection

    @Autowired
    public PlayerProfileController(PlayerProfileService playerProfileService, ClubService clubService) {
        this.playerProfileService = playerProfileService;
        this.clubService = clubService;
    }

    @GetMapping
    public List<PlayerProfile> getPlayerProfiles() {
        return playerProfileService.getPlayerProfiles();
    }

    @PutMapping("/{playerProfileId}/position")
    public ResponseEntity<PlayerProfile> updatePlayerPosition(
        @PathVariable Long playerProfileId, 
        @RequestBody PlayerPositionDTO playerPositionDTO) {
        
        PlayerPosition preferredPosition = playerPositionDTO.getPreferredPosition();
        PlayerProfile updatedProfile = playerProfileService.updatePlayerPosition(playerProfileId, preferredPosition);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/{playerId}/acceptInvitation")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long playerId, @RequestBody AcceptInvitationRequest request) {
        try {
            Club club = clubService.acceptInvite(playerId, request.getClubId());
            return new ResponseEntity<>(club, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
