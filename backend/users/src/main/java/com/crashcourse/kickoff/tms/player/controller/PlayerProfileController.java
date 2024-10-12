package com.crashcourse.kickoff.tms.player.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.player.dto.AcceptInvitationRequest;
import com.crashcourse.kickoff.tms.player.dto.PlayerPositionDTO;
import com.crashcourse.kickoff.tms.player.dto.PlayerProfileUpdateDTO;
import com.crashcourse.kickoff.tms.player.service.PlayerProfileService;
import com.crashcourse.kickoff.tms.security.JwtUtil;
import com.crashcourse.kickoff.tms.user.service.UserService;

@RestController
@RequestMapping("/playerProfiles")
public class PlayerProfileController {

    private final PlayerProfileService playerProfileService;
    private final UserService userService; // final for constructor injection
    private final JwtUtil jwtUtil; // final for constructor injection

    @Autowired
    public PlayerProfileController(PlayerProfileService playerProfileService, JwtUtil jwtUtil,
            UserService userService) {
        this.playerProfileService = playerProfileService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping
    public List<PlayerProfile> getPlayerProfiles() {
        return playerProfileService.getPlayerProfiles();
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getPlayerProfile(@PathVariable Long playerId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        // if (token == null || !token.startsWith("Bearer ")) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid" + token);
        // }
        // token = token.substring(7);
        // // Extract the userId from the token using JwtUtil
        // Long userIdFromToken = jwtUtil.extractUserId(token);
        // if (!userIdFromUsername.equals(userIdFromToken)) {
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this profile");
        // }

        PlayerProfile playerProfile = playerProfileService.getPlayerProfile(playerId);
        if (playerProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PlayerProfile not found");
        }

        return ResponseEntity.ok(playerProfile);
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("@playerProfileService.isOwner(#id, authentication.name)")
    public ResponseEntity<?> updatePlayerProfile(@PathVariable Long id, @RequestBody PlayerProfileUpdateDTO playerProfileUpdateDTO) {
        PlayerProfile playerProfile = playerProfileService.getPlayerProfile(id);

        if (playerProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PlayerProfile not found");
        }

        // The @PreAuthorize annotation ensures only the owner can proceed

        // Update the player profile
        PlayerProfile updatedProfile = playerProfileService.updatePlayerProfile(playerProfile, playerProfileUpdateDTO);

        return new ResponseEntity<>(updatedProfile,HttpStatus.OK);
    }

    // not currently used ("deprecated")
    // @PutMapping("/{playerProfileId}/position")
    // public ResponseEntity<PlayerProfile> updatePlayerPosition(
    //         @PathVariable Long playerProfileId,
    //         @RequestBody PlayerPositionDTO playerPositionDTO) {

    //     PlayerPosition preferredPosition = playerPositionDTO.getPreferredPosition();
    //     PlayerProfile updatedProfile = playerProfileService.updatePlayerPosition(playerProfileId, preferredPosition);
    //     return ResponseEntity.ok(updatedProfile);
    // }

    @PostMapping("/{playerId}/acceptInvitation")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long playerId,
            @RequestBody AcceptInvitationRequest request) {
        try {
            // Club club = clubService.acceptInvite(playerId, request.getClubId());
            // return new ResponseEntity<>(club, HttpStatus.OK);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
