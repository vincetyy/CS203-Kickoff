package com.crashcourse.kickoff.tms.player.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.club.ClubService;
import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.player.dto.AcceptInvitationRequest;
import com.crashcourse.kickoff.tms.player.dto.PlayerPositionDTO;
import com.crashcourse.kickoff.tms.player.service.PlayerProfileService;
import com.crashcourse.kickoff.tms.security.JwtUtil;
import com.crashcourse.kickoff.tms.user.service.UserService;

@RestController
@RequestMapping("/playerProfiles")
public class PlayerProfileController {

    private final PlayerProfileService playerProfileService;
    private final ClubService clubService; // final for constructor injection
    private final UserService userService; // final for constructor injection
    private final JwtUtil jwtUtil; // final for constructor injection

    @Autowired
    public PlayerProfileController(PlayerProfileService playerProfileService, ClubService clubService, JwtUtil jwtUtil,
            UserService userService) {
        this.playerProfileService = playerProfileService;
        this.clubService = clubService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping
    public List<PlayerProfile> getPlayerProfiles() {
        return playerProfileService.getPlayerProfiles();
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getPlayerProfileByUsername(@PathVariable String username,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userIdFromUsername = userService.loadUserByUsername(username).getId();
        // Extract the userId from the token using JwtUtil
        Long userIdFromToken = jwtUtil.extractUserId(token);
        if (userIdFromUsername != userIdFromToken) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this profile");
        }

        PlayerProfile playerProfile = playerProfileService.getPlayerProfile(userIdFromUsername);
        if (playerProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PlayerProfile not found");
        }

        return ResponseEntity.ok(playerProfile);
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
    public ResponseEntity<?> acceptInvitation(@PathVariable Long playerId,
            @RequestBody AcceptInvitationRequest request) {
        try {
            Club club = clubService.acceptInvite(playerId, request.getClubId());
            return new ResponseEntity<>(club, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
