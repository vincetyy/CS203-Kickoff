package com.crashcourse.kickoff.tms.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crashcourse.kickoff.tms.user.dto.PlayerPositionDTO;
import com.crashcourse.kickoff.tms.user.model.PlayerPosition;
import com.crashcourse.kickoff.tms.user.model.PlayerProfile;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.service.PlayerProfileService;
import com.crashcourse.kickoff.tms.user.service.UserService;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/players")
public class PlayerProfileController {

    private final PlayerProfileService playerProfileService;

    public PlayerProfileController(PlayerProfileService playerProfileService) {
        this.playerProfileService = playerProfileService;
    }

    @GetMapping
    public List<PlayerProfile> getPlayerProfiles() {
        return playerProfileService.getPlayerProfiles();
    }
        // Add a new endpoint to update player position
    @PutMapping("/{playerProfileId}/position")
    public ResponseEntity<PlayerProfile> updatePlayerPosition(
        @PathVariable Long playerProfileId, 
        @RequestBody PlayerPositionDTO playerPositionDTO) {
        
        PlayerPosition preferredPosition = playerPositionDTO.getPreferredPosition();
        PlayerProfile updatedProfile = playerProfileService.updatePlayerPosition(playerProfileId, preferredPosition);
        return ResponseEntity.ok(updatedProfile);
    }
}
