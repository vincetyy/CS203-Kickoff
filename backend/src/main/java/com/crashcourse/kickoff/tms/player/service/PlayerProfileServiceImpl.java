package com.crashcourse.kickoff.tms.player.service;

import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.host.HostProfileRepository;
import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.player.respository.PlayerProfileRepository;
import com.crashcourse.kickoff.tms.user.model.User;

import java.util.*;

@Service
public class PlayerProfileServiceImpl implements PlayerProfileService {
    private PlayerProfileRepository playerProfiles;

    public PlayerProfileServiceImpl(PlayerProfileRepository playerProfiles) {
        this.playerProfiles = playerProfiles;
    }

    @Override
    public List<PlayerProfile> getPlayerProfiles() {
        return playerProfiles.findAll();
    }

    @Override
    public PlayerProfile updatePlayerPosition(Long playerProfileId, PlayerPosition preferredPosition) {
        Optional<PlayerProfile> userOpt = playerProfiles.findById(playerProfileId);
        if (userOpt.isPresent()) {
            PlayerProfile playerProfile = userOpt.get();
            if (playerProfile != null) {
                playerProfile.setPreferredPosition(preferredPosition);
                playerProfiles.save(playerProfile);  // Save the user and the updated profile
                return playerProfile;
            } else {
                throw new IllegalArgumentException("Player profile not found for user with id " + playerProfileId);
            }
        } else {
            throw new IllegalArgumentException("User not found with id " + playerProfileId);
        }
    }
}
