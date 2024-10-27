package com.crashcourse.kickoff.tms.player.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.player.dto.PlayerProfileUpdateDTO;
import com.crashcourse.kickoff.tms.player.respository.PlayerProfileRepository;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

import jakarta.transaction.Transactional;

@Service("playerProfileService")
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
    public PlayerProfile getPlayerProfile(Long playerProfileId) {
        return playerProfiles.findById(playerProfileId).orElse(null);
    }

    // not currently used ("deprecated")
    // @Override
    // public PlayerProfile updatePlayerPosition(Long playerProfileId,
    // PlayerPosition preferredPosition) {
    // Optional<PlayerProfile> userOpt = playerProfiles.findById(playerProfileId);
    // if (userOpt.isPresent()) {
    // PlayerProfile playerProfile = userOpt.get();
    // if (playerProfile != null) {
    // playerProfile.setPreferredPositions(Collections.singletonList(preferredPosition));
    // // outdated method to
    // // set to only one
    // // position
    // playerProfiles.save(playerProfile); // Save the user and the updated profile
    // return playerProfile;
    // } else {
    // throw new IllegalArgumentException("Player profile not found for user with id
    // " + playerProfileId);
    // }
    // } else {
    // throw new IllegalArgumentException("User not found with id " +
    // playerProfileId);
    // }
    // }

    private List<PlayerPosition> getListOfPreferredPosition(String[] preferredPositionArray) {
        return Arrays.stream(preferredPositionArray)
                .map(position -> PlayerPosition.valueOf(position.toUpperCase()))
                .collect(Collectors.toList());
    }

    // check if the username in the claim is indeed the profile id in the request variable
    public boolean isOwner(Long profileId, String username) {
        Optional<PlayerProfile> playerProfileOpt = playerProfiles.findById(profileId);
        if (playerProfileOpt.isPresent()) {
            PlayerProfile playerProfile = playerProfileOpt.get();
            return playerProfile.getUser().getUsername().equals(username);
        }
        return false;
    }

    @Transactional
    @Override
    public PlayerProfile updatePlayerProfile(PlayerProfile playerProfile,
            PlayerProfileUpdateDTO playerProfileUpdateDTO) {
        List<PlayerPosition> preferredPositions = getListOfPreferredPosition(
                playerProfileUpdateDTO.getPreferredPositions());
        playerProfile.setPreferredPositions(preferredPositions);
        playerProfile.setProfileDescription(playerProfileUpdateDTO.getProfileDescription());
        return playerProfiles.save(playerProfile);
    }

    @Transactional
    @Override
    public PlayerProfile addPlayerProfile(User newUser, NewUserDTO newUserDTO) {
        PlayerProfile newPlayerProfile = new PlayerProfile();
        List<PlayerPosition> preferredPositions = getListOfPreferredPosition(newUserDTO.getPreferredPositions());
        newPlayerProfile.setPreferredPositions(preferredPositions);
        newPlayerProfile.setUser(newUser);
        return playerProfiles.save(newPlayerProfile);
    }

}
