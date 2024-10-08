package com.crashcourse.kickoff.tms.player.service;

import java.util.List;

import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.player.dto.PlayerProfileUpdateDTO;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

public interface PlayerProfileService {
    List<PlayerProfile> getPlayerProfiles();
    // PlayerProfile updatePlayerPosition(Long userId, PlayerPosition preferredPosition);     // not currently used ("deprecated")
    PlayerProfile getPlayerProfile(Long playerId);
    PlayerProfile addPlayerProfile(User newUser, NewUserDTO newUserDTO);
    PlayerProfile updatePlayerProfile(PlayerProfile playerProfile, PlayerProfileUpdateDTO playerProfileUpdateDTO);
    public boolean isOwner(Long profileId, String username);
}
