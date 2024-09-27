package com.crashcourse.kickoff.tms.user.service;

import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.user.model.PlayerPosition;
import com.crashcourse.kickoff.tms.user.model.PlayerProfile;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.repository.HostProfileRepository;

import java.util.*;

@Service
public class PlayerProfileServiceImpl implements PlayerProfileService {
    private HostProfileRepository playerProfiles;

    public PlayerProfileServiceImpl(HostProfileRepository playerProfiles) {
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
