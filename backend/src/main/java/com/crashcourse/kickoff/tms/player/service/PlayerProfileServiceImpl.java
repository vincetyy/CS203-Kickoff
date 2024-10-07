package com.crashcourse.kickoff.tms.player.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.player.respository.PlayerProfileRepository;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

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
                playerProfile.setPreferredPositions(Collections.singletonList(preferredPosition)); // outdated method to
                                                                                                   // set to only one
                                                                                                   // position
                playerProfiles.save(playerProfile); // Save the user and the updated profile
                return playerProfile;
            } else {
                throw new IllegalArgumentException("Player profile not found for user with id " + playerProfileId);
            }
        } else {
            throw new IllegalArgumentException("User not found with id " + playerProfileId);
        }
    }

    @Override
    public PlayerProfile addPlayerProfile(User newUser, NewUserDTO newUserDTO) {
        PlayerProfile newPlayerProfile = new PlayerProfile();
        List<PlayerPosition> preferredPositions = Arrays.stream(newUserDTO.getPreferredPositions())
                .map(position -> PlayerPosition.valueOf("POSITION_" + position.toUpperCase()))
                .collect(Collectors.toList());
        newPlayerProfile.setPreferredPositions(preferredPositions);
        newPlayerProfile.setUser(newUser);
        return playerProfiles.save(newPlayerProfile);
    }
}
